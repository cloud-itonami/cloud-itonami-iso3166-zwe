(ns marketentry.facts
  "Per-jurisdiction public-procurement market-entry regulatory catalog
  -- the G2-style spec-basis table the Market-Entry Compliance Governor
  checks every `:jurisdiction/assess` proposal against ('did the advisor
  cite an OFFICIAL public source for this jurisdiction's requirements,
  or did it invent one?').

  Zimbabwe's real market-entry surface (curl/WebFetch-verified
  2026-07-23; where a page could not be reached, or turned out to be
  behind a bot-challenge, that is stated explicitly rather than
  silently omitted):

  - **Foreign investment.** The brief for this vertical specifically
    asked to verify, rather than assume, whether the old blanket
    51%-local-ownership 'indigenisation' regime still applies. This
    iteration downloaded and read the actual consolidated Act text (via
    the Internet Archive Wayback Machine, `zimlii.org`'s own individual
    document pages being Cloudflare-JS-challenge-gated to `curl` --
    confirmed universal via `zida-act.html` returning an identical 'Just
    a moment...' HTTP 403 shell, the same access-gap shape prior
    siblings in this family (LSO/GMB/NAM) hit on their own
    jurisdictions' LII sites): the **Zimbabwe Investment and Development
    Agency Act, 2019 [Chapter 14:37]** (commenced 7 February 2020),
    whose own enacting clause reads verbatim: 'AN ACT to provide for the
    promotion, entry, protection and facilitation of investment; to
    provide for the establishment of the Zimbabwe Investment and
    Development Agency; to provide for the One Stop Investment Services
    Centre; to repeal the Zimbabwe Investment Authority Act [Chapter
    14:30], the Special Economic Zones Act [Chapter 14:34] and the Joint
    Ventures Act [Chapter 22:22]...'. This iteration independently
    confirmed the CURRENT state rather than assuming either the old or
    new regime: the ZIDA Act's own section 13 ('Non-discrimination
    between domestic and foreign investors') guarantees foreign
    investors treatment no less favourable than domestic investors,
    EXCEPT it explicitly (and narrowly) carves out 'the existing
    non-conforming measures as set out in section[s] 3 and 3A of the
    Indigenisation and Economic Empowerment Act [Chapter 14:33]' -- i.e.
    the ZIDA Act did NOT repeal the Indigenisation Act wholesale. Reading
    the Indigenisation and Economic Empowerment Act [Chapter 14:33]
    itself (also Wayback-fetched, same access-gap on ZimLII's live
    individual-document route) confirms the ACTUAL current shape of that
    narrowed carve-out, which Act 1 of 2018 substituted for the old
    across-the-board 51% rule: (a) section 3(1) now reads verbatim '...
    secure that at least fifty-one per centum of the shares or other
    ownership interest of every designated extractive business, that is
    to say a company, entity or business involved in the extraction of--
    (a) diamonds; or (b) platinum; shall be owned through an appropriate
    designated entity' -- i.e. the 51% floor survives ONLY for
    diamond/platinum extraction, not for business in general; and (b)
    section 3A(1) ('Reserved sectors of the economy', '[section inserted
    by Act 1 of 2018]') separately reads verbatim 'Subject to subsections
    (2) and (10), only a business owned by a person who is a citizen of
    Zimbabwe may operate in the reserved sector of the economy' -- a
    CLOSED list of 12 specific business classes (the Act's own First
    Schedule, substituted by Act 1 of 2018) reserved entirely for
    Zimbabwean citizens, subject to a narrow grandfather exception
    (s.3A(2)-(3)) for a foreign-owned business that was ALREADY operating
    in a reserved sector before 1 January 2018, PROVIDED it registers
    with the Zimbabwe Revenue Authority and the administering 'Unit' and
    maintains a bank account under the Bank Use Promotion Act [Chapter
    24:24]. Outside these two closed carve-outs, ZIDA's own current
    (2026) public-facing site independently corroborates full
    liberalisation: `zidainvest.com/doing-business-in-zimbabwe/` (fetched
    directly, live, 'Copyright (c) 2026 ZIDA') states verbatim under
    'General Investment License': 'Investors can own up to 100% of their
    business, providing the flexibility and confidence to launch,
    operate, and grow their investment.' This iteration also
    independently confirmed, from ZIMRA's own site (see below), that
    ZIDA is reachable in 2026 at `zidainvest.com` (NOT `zida.co.zw` --
    that domain is a dead/reserved parking page with a mismatched TLS
    certificate for an unrelated host `*.secure-secure.co.uk` -- and NOT
    `zida.org.zw`, which this iteration confirmed via a direct fetch is
    an unrelated organisation, the Zimbabwe Dental Association).
    `ownership-regime-spec-basis` below and the flagship governor check
    (`marketentry.governor`/`marketentry.registry`) are grounded in this
    exact three-way structure (reserved-sector-citizens-only /
    extractive-51%-floor / otherwise-unrestricted).
  - **Company registration.** The Companies Act [Chapter 24:03] was
    itself repealed (confirmed directly from its own successor's
    enacting clause) by the **Companies and Other Business Entities Act,
    2019 [Chapter 24:31]** (commenced 13 February 2020, Wayback-fetched
    from ZimLII, same access-gap discipline as above), administered by
    the **Minister of Justice, Legal and Parliamentary Affairs**
    (confirmed directly from the Act's own interpretation clause: '\"
    Minister \" means the Minister of Justice, Legal and Parliamentary
    Affairs or any other Minister to whom the President may, from time
    to time, assign the administration of this Act'), via the **Office
    for the Registration of Companies and Other Business Entities**
    ('the Companies Office'), headed by a Chief Registrar. This iteration
    independently confirmed the 'one-stop-shop' reform pattern the brief
    asked about rather than assuming it: the ZIDA Act [Chapter 14:37]'s
    own section 5 ('One Stop Investment Services Centre') lists, among
    its statutory desks, '(h) a desk to represent the Office for the
    Registration of Companies and Other Business Entities' alongside
    desks for the Zimbabwe Revenue Authority, the Reserve Bank of
    Zimbabwe, Immigration, NSSA, EMA, ZERA and others, and its own
    subsection (2) explicitly names the whole arrangement 'the one stop
    shop'. ZIDA's own live 2026 site independently corroborates a
    digital counterpart, the 'DIY Investor Licensing Portal'
    (`zidainvest.com/zida-diy-portal/`, fetched directly) for investment
    licensing specifically (company registration proper remains the
    Companies Office's own function; this iteration does not conflate
    the two).
  - **Public procurement.** The **Public Procurement and Disposal of
    Public Assets Act [Chapter 22:23]** (Act No. 5 of 2017, commenced 1
    January 2018, amended by the ZIDA Act on 7 February 2020 --
    Wayback-fetched, own enacting clause read directly) establishes,
    verbatim, 'an authority to be known as the **Procurement Regulatory
    Authority of Zimbabwe**' (PRAZ, section 5) and repeals the old
    'Procurement Act [Chapter 22:14] (No. 2 of 1999)'. PRAZ's own live
    site (`www.praz.org.zw` -- NOT the bare `praz.org.zw`, whose
    certificate only validates for the `www` host; fetched directly,
    HTTP 200, ordinary server-rendered HTML, no bot-challenge)
    independently confirms and dates the national e-procurement portal
    this vertical's `:national-spec` cites: 'The Procurement Regulatory
    Authority of Zimbabwe (PRAZ) introduced the electronic Government
    Procurement (eGP) System... The system was launched by His
    Excellency, President Cde Dr. E.D. Mnangagwa, on 23 October 2023',
    with the same page's own live counters reading '350+ Government
    Entities', '17 000+ Registered Suppliers', '20 000+ Categories
    Registered', '10 000+ Tenders Published'.
  - **Tax registration.** The **Zimbabwe Revenue Authority (ZIMRA)**'s
    own site (`www.zimra.co.zw`, fetched directly, HTTP 200, no
    bot-challenge) confirms the Business Partner Number (BPN)
    taxpayer-ID scheme directly on its own 'Registration (REV1 & REV2)
    Explained' FAQ page, verbatim: 'It is after the submission of the
    Rev 1 that a client is allocated with a Business Partner Number.'
    The Indigenisation Act's own section 3A(3) independently
    corroborates ZIMRA's registration role in an unrelated statutory
    context (the reserved-sector grandfather exception above requires
    registering with 'the Zimbabwe Revenue Authority').
  - **Labour law.** The **Labour Act [Chapter 28:01]** (commenced 15
    December 1985, consolidated to 31 December 2016 with amendments
    published up to 2017 -- confirmed directly from ZimLII's own
    citation page, Wayback-fetched) is cited in `statute.facts` below
    for the general-law catalog, per this vertical's brief.
  - **Currency / exchange control.** The brief specifically warned that
    Zimbabwe's currency history is complex and asked to verify the
    CURRENT state rather than an outdated regime. This iteration did
    NOT assume the old (pre-April-2024) currency remained current: the
    Reserve Bank of Zimbabwe Act [Chapter 22:15] (cited directly from
    the ZIDA Act's own section 5(1)(g), which names 'the Reserve Bank of
    Zimbabwe referred to in section 4 of the Reserve Bank of Zimbabwe
    Act [Chapter 22:15]') is Zimbabwe's central-bank statute, and ZimLII's
    own legislation-listing page (`zimlii.org/legislation/?page=7`,
    fetched directly, plain server-rendered HTML, HTTP 200, NOT gated --
    listing pages are not behind the same Cloudflare challenge as
    individual document pages) names, as a live current instrument,
    'Reserve Bank of Zimbabwe (Issue of Series of New ZiG10, ZiG20,
    ZiG50, ZiG100 and ZiG200 Banknotes) Notice, 2026' dated 27 February
    2026 -- confirming ZiG (introduced April 2024, per the instrument's
    own title naming a 2026 banknote series still denominated in ZiG) is
    the CURRENT currency, not an earlier regime. This iteration could
    NOT independently read the Reserve Bank of Zimbabwe's own live site
    (`www.rbz.co.zw`) beyond this listing-level citation: it returned a
    'Radware Captcha Page' (confirmed by inspecting the page title) --
    a genuine bot-detection challenge this iteration did NOT attempt to
    bypass, per this vertical's hard safety rule. The instrument's own
    full text (beyond its listing title) was likewise not independently
    read this iteration (the Wayback snapshot available for the exact
    document returned a transient 503 from the Archive itself) -- an
    honest gap, not a claim that the full text was read.
  - **Reachability notes (honest disclosure of what did NOT work):**
    `zida.co.zw` / `www.zida.co.zw` resolve in DNS but serve only a
    parked 'This page has been reserved for future use' placeholder
    behind a TLS certificate for an unrelated host
    (`*.secure-secure.co.uk`); `investzim.com` (the pre-2019 Zimbabwe
    Investment Authority's old domain, confirmed via Wayback Machine
    snapshots from 2013-2015) now redirects to an unrelated Portuguese-
    language sports-betting page (domain squatted, confirmed live this
    iteration); `zida.org.zw` is the Zimbabwe Dental Association, not
    the investment agency, confirmed both live (a client-rendered SPA
    shell, 'Please enable JavaScript', LiteSpeed server, no
    Cloudflare/bot-challenge marker) and via a 2022 Wayback snapshot
    whose actual rendered content reads 'Zimbabwe Dental Association --
    Join the leading professional membership body for dentistry in
    Southern Africa'. `zimlii.org`'s own site-search endpoint
    (`/search/?q=...`) returned an empty results shell for this
    iteration's queries (a client-rendered or misconfigured search, not
    usable) -- the legislation LISTING pages (`/legislation/?page=N`,
    paginated 1 through 9, all fetched directly this iteration) were
    used instead, the same discipline the NAM/LSO/GMB catalogs in this
    family used for their own LII sites.

  Coverage is reported HONESTLY (see `coverage`): a jurisdiction not in
  this table has NO spec-basis, full stop -- the advisor must not
  fabricate one, and the governor holds if it tries.")

(def reserved-sectors
  "The Indigenisation and Economic Empowerment Act [Chapter 14:33]'s own
  First Schedule (Section 3A(1), '[Schedule substituted by Act 1 of
  2018]', fetched directly via Wayback Machine from ZimLII), reproduced
  verbatim as a closed set of 12 business classes reserved entirely for
  citizens of Zimbabwe (subject to the narrow pre-2018 grandfather
  exception documented in the namespace docstring, and in
  `marketentry.registry`, which this catalog does not silently ignore
  but also does not pretend to model in full generality)."
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
  "The Indigenisation and Economic Empowerment Act [Chapter 14:33]'s own
  section 3(1) ('designated extractive business'), reproduced verbatim:
  a company, entity or business 'involved in the extraction of-- (a)
  diamonds; or (b) platinum'. These two sectors alone carry the
  surviving 51%-ownership-via-appropriate-designated-entity floor; every
  OTHER extraction/sector is unrestricted per the Zimbabwe Investment
  and Development Agency Act, 2019 [Chapter 14:37]'s own non-
  discrimination guarantee (section 13)."
  #{:diamond-extraction :platinum-extraction})

(def catalog
  "iso3 -> requirement map. `:required-evidence` mirrors the generic
  intake/portal-registration/filing evidence set; `:legal-basis` /
  `:owner-authority` / `:provenance` are the G2 citation the governor
  requires before any `:jurisdiction/assess` proposal can commit. ZWE
  deliberately carries NO `:rep-owner-authority` -- this iteration did
  not independently confirm a Zimbabwe-specific resident-representative
  requirement analogous to South Africa's CSD registration (an honest
  gap, not a claim that no such requirement exists).
  `:reserved-sector-owner-authority` / `:reserved-sector-legal-basis` /
  `:reserved-sector-list` / `:extractive-sectors` /
  `:extractive-ownership-floor-pct` / `:reserved-sector-provenance`
  ground this vertical's flagship governor check
  (`reserved-sector-ownership-mismatch?` in `marketentry.registry`)."
  {"ZWE" {:name "Zimbabwe"
          :owner-authority "Procurement Regulatory Authority of Zimbabwe (PRAZ) -- 'There is hereby established an authority to be known as the Procurement Regulatory Authority of Zimbabwe' (Public Procurement and Disposal of Public Assets Act [Chapter 22:23], section 5, own text, Wayback-fetched from zimlii.org/akn/zw/act/2017/5/eng@2020-02-07); PRAZ's own live site (www.praz.org.zw, fetched directly) independently confirms and operates the PRAZ electronic Government Procurement (eGP) System, 'launched by His Excellency, President Cde Dr. E.D. Mnangagwa, on 23 October 2023'"
          :legal-basis "Public Procurement and Disposal of Public Assets Act [Chapter 22:23] (Act No. 5 of 2017, commenced 1 January 2018, amended by the Zimbabwe Investment and Development Agency Act, 2019 [Chapter 14:37] on 7 February 2020) -- own enacting clause, Wayback-fetched from ZimLII (zimlii.org's live individual-document route is Cloudflare-JS-challenge-gated to curl, confirmed universal by also fetching an unrelated document path returning an identical 'Just a moment...' shell)"
          :national-spec "PRAZ electronic Government Procurement (eGP) System -- PRAZ's own live site (www.praz.org.zw, fetched directly, HTTP 200, no bot-challenge) reports (as of this iteration) '350+ Government Entities', '17 000+ Registered Suppliers', '20 000+ Categories Registered', '10 000+ Tenders Published'"
          :provenance "https://zimlii.org/akn/zw/act/2017/5/eng@2020-02-07 ; https://www.praz.org.zw/"
          :required-evidence ["Certificate of Incorporation / registration record from the Office for the Registration of Companies and Other Business Entities (the 'Companies Office'), Companies and Other Business Entities Act, 2019 [Chapter 24:31] (own enacting text confirms it repeals the old Companies Act [Chapter 24:03]; administered by the Minister of Justice, Legal and Parliamentary Affairs per the Act's own interpretation clause) -- Wayback-fetched from zimlii.org/akn/zw/act/2019/4/eng@2019-11-15"
                              "Zimbabwe Revenue Authority (ZIMRA) Business Partner Number (BPN) allocation record, issued upon submission of form REV 1 -- ZIMRA's own 'Registration (REV1 & REV2) Explained' FAQ page (www.zimra.co.zw, fetched directly): 'It is after the submission of the Rev 1 that a client is allocated with a Business Partner Number'"
                              "PRAZ eGP System supplier-registration record (www.praz.org.zw, fetched directly, own site: 'The system was launched by His Excellency, President Cde Dr. E.D. Mnangagwa, on 23 October 2023')"
                              "Reserved-sector / extractive-ownership determination record, when the engagement declares :seeking-ownership-determination? true"]
          :corporate-number-owner-authority "Zimbabwe Revenue Authority (ZIMRA)"
          :corporate-number-legal-basis "Business Partner Number (BPN) taxpayer-ID, allocated upon submission of form REV 1 -- confirmed directly from ZIMRA's own 'Registration (REV1 & REV2) Explained' FAQ page (www.zimra.co.zw, fetched directly, HTTP 200, no bot-challenge): 'It is after the submission of the Rev 1 that a client is allocated with a Business Partner Number. If there are any changes to details previously submitted by the client, then the client should complete and submit a Rev 2...'"
          :corporate-number-provenance "https://www.zimra.co.zw/domestic-taxes/corporate/registration-rev1-rev2-explained"
          :reserved-sector-owner-authority "Indigenisation and Economic Empowerment Act [Chapter 14:33] administering Minister / Unit (own text, section 3A) -- confirmed the CURRENT narrowed regime rather than assuming the old blanket rule still applies: 'Subject to subsections (2) and (10), only a business owned by a person who is a citizen of Zimbabwe may operate in the reserved sector of the economy'; the Zimbabwe Investment and Development Agency Act, 2019 [Chapter 14:37]'s own section 13 non-discrimination guarantee explicitly does NOT override this measure"
          :reserved-sector-legal-basis "Indigenisation and Economic Empowerment Act [Chapter 14:33], section 3A ('Reserved sectors of the economy', section inserted by Act 1 of 2018) + First Schedule ('Reserved/threshold sectors', schedule substituted by Act 1 of 2018) -- own text, Wayback-fetched from zimlii.org/akn/zw/act/2007/14/eng@2018-03-14 (ZimLII's own 'Is amended by: Finance (No. 2) Act, 2020' relation confirms this Act was amended again after the 2018-03-14 consolidated text this iteration read, but that later amendment's own text was not independently read this iteration -- an honest gap)"
          :reserved-sector-list reserved-sectors
          :extractive-sectors extractive-sectors
          :extractive-ownership-floor-pct 51
          :reserved-sector-provenance "https://zimlii.org/akn/zw/act/2007/14/eng@2018-03-14 ; https://zidainvest.com/doing-business-in-zimbabwe/"}
   "USA" {:name "United States"
          :owner-authority "U.S. General Services Administration (GSA) / SAM.gov"
          :legal-basis "Federal Acquisition Regulation (FAR); System for Award Management"
          :national-spec "SAM.gov entity registration + NAICS self-certification"
          :provenance "https://sam.gov/"
          :required-evidence ["EIN record"
                              "SAM.gov registration record"
                              "State business registration record"
                              "Authorized-representative record"]}})

(defn spec-basis
  "The jurisdiction's requirement map, or nil -- nil means NO spec-basis,
  and the governor must hold any proposal that tries to assess or file
  on it."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report: how many of the requested jurisdictions actually
  have a spec-basis entry. Never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-zwe R0: " (count catalog)
                 " jurisdictions seeded with an official spec-basis. "
                 "This is a starting catalog for market-entry navigation, "
                 "not a survey of all ~194 jurisdictions -- extend "
                 "`marketentry.facts/catalog`, never fabricate a "
                 "jurisdiction's requirements.")})))

(defn required-evidence-satisfied?
  "Does `submitted` (a set/coll of evidence keywords or strings) satisfy
  every evidence item listed for `iso3`? Missing spec-basis -> never
  satisfied."
  [iso3 submitted]
  (when-let [{:keys [required-evidence]} (spec-basis iso3)]
    (let [need (count required-evidence)
          have (count (filter (set submitted) required-evidence))]
      (= need have))))

(defn evidence-checklist [iso3] (:required-evidence (spec-basis iso3) []))

(defn rep-spec-basis
  "The jurisdiction's representative-related requirement map, or nil when
  this catalog has no such regime. For ZWE this is deliberately nil --
  see the `catalog` docstring's honest-scope-narrowing note (no
  Zimbabwe-specific resident-representative provision was independently
  confirmed this iteration)."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:rep-owner-authority sb)
      (select-keys sb [:rep-owner-authority :rep-legal-basis :rep-provenance]))))

(defn corporate-number-spec-basis
  "The jurisdiction's corporate-number / tax-id regime, or nil."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:corporate-number-owner-authority sb)
      (select-keys sb [:corporate-number-owner-authority
                       :corporate-number-legal-basis
                       :corporate-number-provenance]))))

(defn ownership-regime-spec-basis
  "The jurisdiction's reserved-sector / extractive-ownership regime, or
  nil. For ZWE this is real and current -- the flagship check this
  vertical adds is grounded here (Indigenisation and Economic
  Empowerment Act [Chapter 14:33], sections 3 and 3A, as narrowed by
  Act 1 of 2018 and expressly preserved -- not overridden -- by the
  Zimbabwe Investment and Development Agency Act, 2019 [Chapter
  14:37]'s own non-discrimination guarantee)."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:reserved-sector-owner-authority sb)
      (select-keys sb [:reserved-sector-owner-authority
                       :reserved-sector-legal-basis
                       :reserved-sector-list
                       :extractive-sectors
                       :extractive-ownership-floor-pct
                       :reserved-sector-provenance]))))
