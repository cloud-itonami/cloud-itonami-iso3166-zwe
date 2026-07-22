(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "ZWE" 0)
        s (registry/register-submit "eng-1" "ZWE" 0)]
    (is (= "ZWE-DFT-000000" (get d "draft_number")))
    (is (= "ZWE-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "ZWE" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest required-domestic-ownership-lookup
  (testing "reserved sectors require full (100%) Zimbabwean-citizen ownership absent a grandfather exemption"
    (is (= 100 (registry/compute-required-domestic-ownership-pct :retail-and-wholesale-trade false)))
    (is (= 100 (registry/compute-required-domestic-ownership-pct :artisanal-mining nil))))
  (testing "the pre-2018 grandfather exemption zeroes the reserved-sector floor"
    (is (= 0 (registry/compute-required-domestic-ownership-pct :retail-and-wholesale-trade true))))
  (testing "extractive sectors carry the surviving 51% floor regardless of the reserved-sector grandfather flag"
    (is (= 51 (registry/compute-required-domestic-ownership-pct :diamond-extraction false)))
    (is (= 51 (registry/compute-required-domestic-ownership-pct :platinum-extraction true))))
  (testing "every other declared sector is unrestricted"
    (is (= 0 (registry/compute-required-domestic-ownership-pct :software-development false))))
  (testing "a missing sector never guesses a floor"
    (is (nil? (registry/compute-required-domestic-ownership-pct nil false)))))

(deftest ownership-matches-claim
  (testing "a claim that EXACTLY matches the independently recomputed compliance verdict matches"
    (is (true? (registry/ownership-matches-claim?
                {:sector :diamond-extraction :declared-domestic-ownership-pct 51.0
                 :claimed-ownership-compliant? true})))
    (is (true? (registry/ownership-matches-claim?
                {:sector :software-development :declared-domestic-ownership-pct 0.0
                 :claimed-ownership-compliant? true}))))
  (testing "a claim opposite to the independently recomputed verdict does not match"
    (is (false? (registry/ownership-matches-claim?
                 {:sector :retail-and-wholesale-trade :declared-domestic-ownership-pct 40.0
                  :reserved-sector-grandfathered? false
                  :claimed-ownership-compliant? true}))
        "40% domestic ownership does not satisfy the reserved sector's 100% floor -- claim of true is wrong"))
  (testing "a grandfathered reserved-sector business correctly claims compliance despite partial domestic ownership"
    (is (true? (registry/ownership-matches-claim?
                {:sector :retail-and-wholesale-trade :declared-domestic-ownership-pct 40.0
                 :reserved-sector-grandfathered? true
                 :claimed-ownership-compliant? true}))))
  (testing "missing sector fails closed"
    (is (false? (registry/ownership-matches-claim?
                 {:declared-domestic-ownership-pct 100.0 :claimed-ownership-compliant? true})))))

(deftest reserved-sector-ownership-mismatch-claim-is-entity-scope-gated
  (testing "an engagement NOT seeking an ownership determination is never flagged, even if the claim is wrong"
    (is (false? (registry/reserved-sector-ownership-mismatch-claim?
                 {:seeking-ownership-determination? false
                  :sector :retail-and-wholesale-trade :declared-domestic-ownership-pct 40.0
                  :reserved-sector-grandfathered? false :claimed-ownership-compliant? true}))))
  (testing "a determination-seeking engagement whose claim does NOT match the independently recomputed verdict -> mismatch"
    (is (true? (registry/reserved-sector-ownership-mismatch-claim?
                {:seeking-ownership-determination? true
                 :sector :retail-and-wholesale-trade :declared-domestic-ownership-pct 40.0
                 :reserved-sector-grandfathered? false :claimed-ownership-compliant? true}))))
  (testing "a determination-seeking engagement whose claim DOES match -> not flagged"
    (is (false? (registry/reserved-sector-ownership-mismatch-claim?
                 {:seeking-ownership-determination? true
                  :sector :retail-and-wholesale-trade :declared-domestic-ownership-pct 40.0
                  :reserved-sector-grandfathered? false :claimed-ownership-compliant? false})))))
