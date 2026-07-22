(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `reserved-sector-ownership-mismatch-claim?` (and the two-tier lookup
  it is built on, `compute-required-domestic-ownership-pct` /
  `ownership-matches-claim?`) is the SAME discipline applied to a
  genuinely Zimbabwe-specific mechanism: the Indigenisation and Economic
  Empowerment Act [Chapter 14:33]'s own post-2018 ownership regime
  (sections 3 and 3A, own text fetched via the Internet Archive Wayback
  Machine from zimlii.org -- see `marketentry.facts` namespace docstring
  for the full research trail and reachability disclosure), which the
  Zimbabwe Investment and Development Agency Act, 2019 [Chapter 14:37]'s
  own non-discrimination guarantee (section 13) expressly does NOT
  override.

  This is a GENUINELY DIFFERENT check SHAPE than every prior iso3166
  sibling this repo mirrors: LSO's Contractors Registration Certificate
  is a discrete-category -> fixed-constant lookup (one dimension), GMB's
  GIEPA Special Investment Certificate is an origin-conditional
  investment-amount MINIMUM (a bidder must MEET OR EXCEED it to become
  eligible for a benefit), NAM's CPBN Board-routing check is a
  two-dimensional (category x contract-type) threshold CEILING that
  changes procedural control once exceeded, CAF's Marche reserve is a
  multi-criterion eligibility test, and Estonia's digital-signing check
  tests the filing's own execution instrument. Zimbabwe's ownership
  regime is none of these: it is a CATEGORICAL, THREE-WAY classifier
  keyed on the engagement's own declared business SECTOR (not a
  contract value or an investment amount) --
    - a CLOSED 12-sector list (`marketentry.facts/reserved-sectors`)
      where the required minimum domestic-ownership floor is 100%
      (full reservation for Zimbabwean citizens), subject to a narrow
      pre-2018-operation-plus-ZIMRA-registration-plus-bank-account
      grandfather exception (Indigenisation Act section 3A(2)-(3));
    - a 2-sector extractive list (diamond/platinum extraction) where the
      floor is 51% (an 'appropriate designated entity' must hold at
      least that much, per section 3(1)) -- narrower in scope than the
      OLD blanket 51% rule this Act 1-of-2018 amendment replaced, a
      distinction this vertical's research specifically set out to
      verify rather than assume;
    - every OTHER sector, where the floor is 0% -- i.e. up to 100%
      foreign ownership is permitted, per the ZIDA Act's own
      non-discrimination guarantee (independently corroborated by
      ZIDA's own live 2026 site: 'Investors can own up to 100% of their
      business').
  The engagement declares its own believed compliance
  (`:claimed-ownership-compliant?`) for a business whose own declared
  `:sector` (and, for a reserved-sector business, its own declared
  `:reserved-sector-grandfathered?` ground truth) it also declares, and
  the governor independently recomputes the TRUE required floor from
  the published regime and compares the engagement's own declared
  `:declared-domestic-ownership-pct` against it for EQUALITY of the
  compliance VERDICT (not the raw percentage) -- catching an operator
  who misclassifies a business's sector, or overstates its domestic
  ownership, to make an otherwise-restricted business look compliant.

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real procurement portal. It builds the RECORD an
  operator would keep, not the act of submitting a portal registration
  itself (that is `marketentry.operation`'s `:filing/submit`, always
  human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  (+ (double base-fee)
     (* (double monthly-rate) (double monitoring-months))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (== (double claimed-fee) (compute-engagement-fee engagement)))

(def reserved-sectors
  "Indigenisation and Economic Empowerment Act [Chapter 14:33], First
  Schedule (Section 3A(1), 'schedule substituted by Act 1 of 2018',
  fetched directly via Wayback Machine from ZimLII): a CLOSED set of 12
  business classes reserved entirely for citizens of Zimbabwe. See
  `marketentry.facts/reserved-sectors` (the same set, duplicated here so
  this namespace's recomputation is independent of the advisor-facing
  catalog, the same discipline NAM's `board-routing-threshold-matrix`
  uses)."
  #{:passenger-transport-taxis-car-hire
    :retail-and-wholesale-trade
    :barbershop-hairdressing-beauty-salon
    :employment-agency
    :estate-agency
    :valet-services
    :grain-milling
    :bakeries
    :tobacco-grading-and-packaging
    :advertising-agency
    :local-arts-and-crafts-marketing-and-distribution
    :artisanal-mining})

(def extractive-sectors
  "Indigenisation and Economic Empowerment Act [Chapter 14:33], section
  3(1) ('designated extractive business'): diamond and platinum
  extraction alone carry the surviving 51% ownership-via-designated-
  entity floor."
  #{:diamond-extraction :platinum-extraction})

(def reserved-sector-ownership-floor-pct
  "Full reservation: only a Zimbabwean-citizen-owned business may
  operate in a reserved sector (Indigenisation Act section 3A(1)),
  subject to the grandfather exception `compute-required-domestic-
  ownership-pct` applies via its own `grandfathered?` argument."
  100)

(def extractive-ownership-floor-pct
  "Indigenisation Act section 3(1): 'at least fifty-one per centum of
  the shares or other ownership interest' for diamond/platinum
  extraction."
  51)

(defn compute-required-domestic-ownership-pct
  "The ground-truth minimum domestic-ownership percentage for
  `sector`, independently recomputed from the Indigenisation and
  Economic Empowerment Act [Chapter 14:33]'s own published regime:
    - nil `sector` -> nil (never guesses a floor for missing data);
    - an extractive sector -> 51 (section 3(1)), regardless of
      `grandfathered?` (the grandfather exception is scoped by the
      Act's own text to the RESERVED sector only, section 3A(2)-(3),
      not to extraction);
    - a reserved sector with `grandfathered?` true -> 0 (the pre-2018-
      operation + ZIMRA-registration + bank-account exception, section
      3A(2)-(3), bundled into one ground-truth boolean the engagement
      declares -- see `marketentry.store` demo-data);
    - a reserved sector otherwise -> 100 (section 3A(1));
    - any other declared sector -> 0 (unrestricted, per the Zimbabwe
      Investment and Development Agency Act, 2019 [Chapter 14:37]'s own
      non-discrimination guarantee, section 13 -- the CONFIRMED default
      rule, not an unconfirmed guess)."
  [sector grandfathered?]
  (cond
    (nil? sector) nil
    (contains? extractive-sectors sector) extractive-ownership-floor-pct
    (and (contains? reserved-sectors sector) (true? grandfathered?)) 0
    (contains? reserved-sectors sector) reserved-sector-ownership-floor-pct
    :else 0))

(defn ownership-matches-claim?
  "Does `engagement`'s own `:claimed-ownership-compliant?` equal the
  INDEPENDENTLY recomputed compliance verdict for its own declared
  `:sector` / `:declared-domestic-ownership-pct` /
  `:reserved-sector-grandfathered?`? An unrecognized (nil) sector fails
  closed (does not throw, never guesses). NOTE: `compute-required-
  domestic-ownership-pct` legitimately returns `0` (not just nil) for an
  unrestricted sector or a grandfathered reserved-sector business -- this
  function uses `some?`, not `when-let`/`if-let`, to distinguish that
  valid `0` from the nil 'missing sector' case (the same discipline
  NAM's `board-routing-matches-claim?` uses for its own valid-`false`-
  vs-nil distinction)."
  [{:keys [sector declared-domestic-ownership-pct
           reserved-sector-grandfathered? claimed-ownership-compliant?]}]
  (let [required (compute-required-domestic-ownership-pct sector reserved-sector-grandfathered?)]
    (boolean (and (some? required)
                  (= (boolean claimed-ownership-compliant?)
                     (>= (double (or declared-domestic-ownership-pct 0))
                         (double required)))))))

(defn reserved-sector-ownership-mismatch-claim?
  "Does `engagement` declare `:seeking-ownership-determination? true`
  (i.e. it is asking this actor to confirm whether its own declared
  sector/domestic-ownership-pct combination is compliant with the
  Indigenisation Act's reserved-sector/extractive-ownership regime)
  while the INDEPENDENTLY recomputed `ownership-matches-claim?` is
  false? An engagement not seeking an ownership determination is never
  flagged by this check (entity/engagement-scope-gated, the same
  discipline LSO's `:seeking-contractor-registration?`-gated check and
  NAM's `:seeking-board-routing-determination?`-gated check use)."
  [{:keys [seeking-ownership-determination?] :as engagement}]
  (boolean (and seeking-ownership-determination?
                (not (ownership-matches-claim? engagement)))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a portal registration
  package. Pure function -- does not touch any real procurement
  portal."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a portal
  registration (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
