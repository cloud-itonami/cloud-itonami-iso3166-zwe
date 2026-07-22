(ns statute.facts-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [statute.facts :as facts]))

(deftest zwe-has-spec-basis
  (let [sb (facts/spec-basis "ZWE")]
    (is (= 3 (count sb)))
    (is (every? #(str/starts-with? (:statute/url %) "https://") sb))
    (is (every? :statute/law-number sb))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["ZWE" "JPN" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ATL" "JPN"] (:missing-jurisdictions c)))))

(deftest by-topic-filters
  (is (= ["zwe.labour-act"]
         (mapv :statute/id (facts/by-topic "ZWE" :labor))))
  (is (= ["zwe.income-tax-act"]
         (mapv :statute/id (facts/by-topic "ZWE" :tax))))
  (is (= ["zwe.companies-and-other-business-entities-act-2019"]
         (mapv :statute/id (facts/by-topic "ZWE" :corporate-governance))))
  (is (empty? (facts/by-topic "ATL" :labor))))
