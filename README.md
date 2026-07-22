# cloud-itonami-iso3166-zwe

Open ISO 3166 Blueprint for **ZWE**: Zimbabwe.

This repository designs a forkable OSS business for an independent
public-sector market-entry consultant: an already-incorporated operator
(e.g. a `cloud-itonami-cofog-{code}`, `cloud-itonami-isco-{code}`,
`cloud-itonami-unspsc-{segment}` or `cloud-itonami-{ISIC}` blueprint
fork) gets a Compliance Advisor + independent **Market-Entry Compliance
Governor** to navigate public-procurement registration, local business/
tax registration, and reserved-sector/ownership rules in Zimbabwe, so
the operator can win and service a government contract without hiring a
full in-house compliance department.

## No robotics premise — digital/data service exemption

Market-entry and procurement-compliance navigation is a pure data/software
service with no physical-domain work (portal registration, document
checklists, regulatory-change monitoring) — the same exemption class as
`cloud-itonami-6310` (HR SaaS replacement) and `cloud-itonami-gtin-*`.
`blueprint.edn` sets `:itonami.blueprint/robotics false` and
`:required-technologies` lists only real capabilities (`:identity`,
`:forms`, `:dmn`, `:bpmn`, `:audit-ledger`), no `:robotics`.

## Core Contract

```text
operator intake + prior filing history
        |
        v
Compliance Advisor -> Market-Entry Compliance Governor -> filing draft, or human sign-off
        |
        v
gated portal registration / filing submission + audit ledger
```

No automated proposal can submit a portal registration or filing the
governor refuses, suppress a compliance record, or claim a legal/tax
conclusion the governor has not cleared. `:filing/submit` is never in any
phase's `:auto` set — it always requires human sign-off.

## What this is NOT

- **Not the government of Zimbabwe.** See
  [`docs/business-model.md`](docs/business-model.md) for the boundary with
  `com-etzhayyim-ooyake` (read-only civic mirror), `matsurigoto` (sovereign
  statecraft), `com-etzhayyim-toritsugi` (individual citizen concierge),
  `legal-entity.etzhayyim.com` (read-only data aggregation), and
  `cloud-itonami-M6910` (company incorporation — a different regulatory
  phase this blueprint assumes is already complete).
- **Not legal or tax advice.** Every regulatory claim must cite the
  official source and route final filings to Zimbabwe-licensed counsel
  or a registered agent where the law requires licensed representation.

## Capability layer

Resolves via [`kotoba-lang/iso3166`](https://github.com/kotoba-lang/iso3166)
(ISO 3166 `ZWE`). Required capabilities:

- :identity
- :forms
- :dmn
- :bpmn
- :audit-ledger

See [`docs/business-model.md`](docs/business-model.md) and
[`docs/operator-guide.md`](docs/operator-guide.md).

## License

AGPL-3.0-or-later.

## Market-entry / statute catalogs

Governed public-sector market-entry compliance actor, same architecture
as every `cloud-itonami-iso3166-*` sibling in this fleet:

- `src/marketentry/{facts,governor,phase,sim,operation,registry,store,
  marketentryllm}.cljc` -- the actor. `facts.cljc` cites the Procurement
  Regulatory Authority of Zimbabwe (PRAZ, established by the Public
  Procurement and Disposal of Public Assets Act [Chapter 22:23], Act No.
  5 of 2017), whose own live site (`www.praz.org.zw`) operates the PRAZ
  electronic Government Procurement (eGP) System launched 23 October
  2023; the Office for the Registration of Companies and Other Business
  Entities (the "Companies Office", Companies and Other Business
  Entities Act, 2019 [Chapter 24:31], administered by the Minister of
  Justice, Legal and Parliamentary Affairs) for company registration;
  and the Zimbabwe Revenue Authority (ZIMRA)'s Business Partner Number
  (BPN) taxpayer-ID scheme, allocated on submission of form REV 1.
  `governor.cljc`'s flagship check independently recomputes whether an
  engagement's own claimed ownership-compliance verdict matches the
  Indigenisation and Economic Empowerment Act [Chapter 14:33]'s own
  current, post-2018 reserved-sector/extractive-ownership regime for its
  own declared business sector -- a genuinely different check SHAPE
  from every prior iso3166 sibling: a categorical, sector-keyed
  three-way classifier (full 100%-domestic-ownership reservation for a
  closed 12-sector list; a narrower 51%-domestic-ownership floor
  surviving ONLY for diamond/platinum extraction; unrestricted, up to
  100% foreign ownership, for every other sector). This vertical's own
  research specifically set out to verify, rather than assume, whether
  the OLD blanket 51% indigenisation rule still applies -- it does not:
  the Zimbabwe Investment and Development Agency Act, 2019 [Chapter
  14:37] (commenced 7 February 2020) narrowed it via its own 2018
  amendment and now guarantees non-discriminatory treatment to foreign
  investors EXCEPT for those two closed carve-outs, independently
  corroborated by ZIDA's own live 2026 site: "Investors can own up to
  100% of their business." See the namespace docstrings for the full
  research trail and honestly-narrowed scope, including facts this
  iteration could NOT verify: no Zimbabwe-specific resident-
  representative requirement was independently confirmed (an honest
  gap, so `rep-spec-basis` is deliberately nil); the Reserve Bank of
  Zimbabwe's own live site (`www.rbz.co.zw`) returned a Radware CAPTCHA
  challenge this iteration did NOT attempt to bypass, so the ZiG
  currency's current status is confirmed only via a ZimLII legislation-
  listing citation (a February 2026 Reserve Bank of Zimbabwe notice
  naming new ZiG banknotes), not RBZ's own primary text.
- `src/statute/facts.cljc` -- general-law catalog: the Companies and
  Other Business Entities Act, 2019 [Chapter 24:31] (which itself
  repealed the old Companies Act [Chapter 24:03]); the Labour Act
  [Chapter 28:01]; and the Income Tax Act [Chapter 23:06]. Titles/
  chapter-numbers/dates are confirmed directly from the Zimbabwe Legal
  Information Institute's (ZimLII, `zimlii.org`) own paginated
  legislation-listing pages (checked in full, pages 1 through 8) and,
  for the Zimbabwe Investment and Development Agency Act and the
  Indigenisation and Economic Empowerment Act specifically, each Act's
  own full consolidated text via Internet Archive Wayback Machine
  snapshots of ZimLII's own document URLs (ZimLII's live individual-
  document route is Cloudflare-JS-challenge-gated to direct `curl`
  fetches, confirmed universal via a control-path test -- the same
  access-gap shape prior siblings in this family hit on their own
  jurisdictions' LII sites). No standalone Value Added Tax Act entry
  was found anywhere across the legislation-listing pages this
  iteration checked -- an honest gap, not modeled.

Every citation is curl/WebFetch-verified against an official source
(www.praz.org.zw, www.zimra.co.zw, zidainvest.com, zimlii.org, and two
Acts' own full text read via Wayback Machine snapshots of ZimLII's
document URLs). See `marketentry.facts`'s and `statute.facts`'s
docstrings for the full disclosure of what this iteration could and
could not independently confirm, including several dead ends this
iteration ruled out directly rather than guessing past: `zida.co.zw` is
a parked placeholder page (mismatched TLS certificate for an unrelated
host), the old `investzim.com` domain now redirects to an unrelated
sports-betting site, and `zida.org.zw` is the Zimbabwe Dental
Association, not the investment agency -- the real, current (2026) ZIDA
site is `zidainvest.com`.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Zimbabwe:

- `src/culture/facts.cljc` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
