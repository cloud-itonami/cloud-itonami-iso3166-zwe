(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest zwe-has-spec-basis
  (let [sb (facts/spec-basis "ZWE")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/ownership-regime-spec-basis "ZWE")))))

(deftest zwe-rep-spec-basis-is-honestly-absent
  (testing "no Zimbabwe-specific resident-representative provision was confirmed this iteration -- deliberately not claimed"
    (is (nil? (facts/rep-spec-basis "ZWE")))))

(deftest zwe-corporate-number-spec-basis-is-present
  (testing "ZIMRA's own site confirms the Business Partner Number (BPN) scheme directly"
    (is (some? (facts/corporate-number-spec-basis "ZWE")))
    (is (= "Zimbabwe Revenue Authority (ZIMRA)"
           (:corporate-number-owner-authority (facts/corporate-number-spec-basis "ZWE"))))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "ZWE")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "ZWE" all)))
    (is (not (facts/required-evidence-satisfied? "ZWE" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["ZWE" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))

(deftest ownership-regime-spec-basis-shape
  (let [orb (facts/ownership-regime-spec-basis "ZWE")]
    (is (= 12 (count (:reserved-sector-list orb))))
    (is (contains? (:reserved-sector-list orb) :retail-and-wholesale-trade))
    (is (contains? (:reserved-sector-list orb) :artisanal-mining))
    (is (= #{:diamond-extraction :platinum-extraction} (:extractive-sectors orb)))
    (is (= 51 (:extractive-ownership-floor-pct orb)))))
