(ns culture.facts
  "Country-level regional-culture catalog for Zimbabwe (ZWE) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"ZWE"
   [{:culture/id "zwe.dish.sadza"
     :culture/name "Sadza"
     :culture/name-local "Isitshwala"
     :culture/country "ZWE"
     :culture/kind :dish
     :culture/summary "Cooked maize meal, called sadza in Shona or isitshwala in isiNdebele, that is the staple food in Zimbabwe."
     :culture/url "https://en.wikipedia.org/wiki/Ugali"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "zwe.dish.mopane-worm"
     :culture/name "Mopane worm"
     :culture/country "ZWE"
     :culture/kind :dish
     :culture/summary "Edible caterpillar of the moth Gonimbrasia belina, consumed as food across southern Africa including Zimbabwe, where it is known in Shona as madora, masodya or mashonja."
     :culture/url "https://en.wikipedia.org/wiki/Gonimbrasia_belina"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "zwe.dish.delele"
     :culture/name "Delele"
     :culture/country "ZWE"
     :culture/kind :dish
     :culture/summary "Zimbabwean, Zambian, north-eastern Botswanan and Northern South African dish made from a local plant of the same name."
     :culture/url "https://en.wikipedia.org/wiki/Delele"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "zwe.beverage.chibuku"
     :culture/name "Chibuku"
     :culture/country "ZWE"
     :culture/kind :beverage
     :culture/summary "Commercial sorghum beer based on traditional Umqombothi homemade African beers; originated in Zambia and is produced in Zimbabwe by Delta Beverages among other African countries."
     :culture/url "https://en.wikipedia.org/wiki/Chibuku_Shake_Shake"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "zwe.craft.zimbabwe-bird"
     :culture/name "Zimbabwe Bird"
     :culture/country "ZWE"
     :culture/kind :craft
     :culture/summary "Stone-carved soapstone bird sculptures recovered from the medieval city of Great Zimbabwe; the Zimbabwe Bird is the national emblem of Zimbabwe, appearing on the national flag and coat of arms."
     :culture/url "https://en.wikipedia.org/wiki/Zimbabwe_Bird"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "zwe.craft.shona-sculpture"
     :culture/name "Shona sculpture"
     :culture/country "ZWE"
     :culture/kind :craft
     :culture/summary "Sculpture, and in particular stone sculpture, is an art for which Zimbabwe is well known around the world."
     :culture/url "https://en.wikipedia.org/wiki/Sculpture_of_Zimbabwe"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "zwe.heritage.great-zimbabwe"
     :culture/name "Great Zimbabwe"
     :culture/country "ZWE"
     :culture/kind :heritage
     :culture/summary "Ruined medieval city recognised as a UNESCO World Heritage Site, designated in 1986 under the official name Great Zimbabwe National Monument."
     :culture/url "https://en.wikipedia.org/wiki/Great_Zimbabwe"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-zwe culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "ZWE"))
                 " ZWE entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
