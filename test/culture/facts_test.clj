(ns culture.facts-test
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [culture.facts :as facts]))

(deftest zwe-has-culture-basis
  (let [sb (facts/spec-basis "ZWE")]
    (is (= 7 (count sb)))
    (is (= (count sb) (count (set (map :culture/id sb)))))
    (is (every? #(str/starts-with? (:culture/url %) "https://") sb))
    (is (every? #(= "ZWE" (:culture/country %)) sb))
    (is (every? #(nil? (:culture/municipality %)) sb))
    (is (every? #(seq (:culture/summary %)) sb))
    (is (every? #(string? (:culture/retrieved-at %)) sb))))

(deftest unknown-jurisdiction-has-no-basis
  (is (nil? (facts/spec-basis "ZMB")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["ZWE" "ZMB"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ZMB"] (:missing-jurisdictions c)))))

(deftest by-kind-filters
  (is (= 3 (count (facts/by-kind "ZWE" :dish))))
  (is (= ["zwe.craft.zimbabwe-bird" "zwe.craft.shona-sculpture"]
         (mapv :culture/id (facts/by-kind "ZWE" :craft))))
  (is (empty? (facts/by-kind "ZWE" :other)))
  (is (empty? (facts/by-kind "ZMB" :dish))))

(deftest tx-file-matches-catalog
  (let [tx (edn/read-string (slurp "data/culture-tx.edn"))
        flat (mapcat val (sort-by key facts/catalog))]
    (is (= (vec flat) (vec tx)))))
