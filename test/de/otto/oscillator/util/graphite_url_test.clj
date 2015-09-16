(ns de.otto.oscillator.util.graphite-url-test
  (:require [clojure.test :refer :all]
            [de.otto.oscillator.util.graphite-url :refer :all]))

(deftest param-string-test
  (testing "it joins params to a query string"
    (is (= "c=3&b=xyz&a=1"
           (param-string {:c 3 :b "xyz" :a 1}))))
  (testing "it recognizes sub-arrays and handles them properly"
    (is (= "c=3&c=4&b=xyz&a=1"
           (param-string {:c '(3 4) :b "xyz" :a 1})))))

(deftest param-tuples-test
  (testing "it joins tuples of a string"
    (is (= ["c=3" "b=xyz" "a=1"]
           (param-tuples {:c 3 :b "xyz" :a 1}))))
  (testing "it recognizes sub-arrays and handles them properly"
    (is (= ["c=3" "c=4" "b=xyz" "a=1"]
           (param-tuples {:c '(3 4) :b "xyz" :a 1})))))

(deftest move-time-test
  (testing "it applies a given function"
    (is (= "-4h"
           (move-time "-6h" 2)))
    (is (= "-1h"
           (move-time "-2h" 1)))
    (is (= "-24h"
           (move-time "24h" -48))))
  (testing "it does not crash if current-time is missing"
    (is (= "-4h"
           (move-time "" -4)))
    (is (= "-4h"
           (move-time "30m" -4)))
    (is (= "-4h"
           (move-time nil -4))))
  (testing "positive results will be mapped to -1min"
    (is (= "-1min"
           (move-time "" 0)
           (move-time "-1h" 2)))))

(deftest parse-hours-test
  (testing "it finds hours within string"
    (is (= -4
           (parse-hours "-4h"))))
  (testing "it handles strange inputs"
    (is (= 0
           (parse-hours "30m")
           (parse-hours "abc")
           (parse-hours "")
           (parse-hours nil)))))
