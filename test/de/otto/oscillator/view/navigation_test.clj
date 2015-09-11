(ns de.otto.oscillator.view.navigation-test
  (:require [clojure.test :refer :all]
            [de.otto.oscillator.view.navigation :as nav]))

(deftest is-active-test
  (testing "it checks if the second maps is completed contained in the first map"
    (is (= true
           (nav/params-active? {:c 3 :b "xyz" :a 1} {:c 3})))
    (is (= true
           (nav/params-active? {:c 3 :b "xyz" :a 1} {:c 3 :b "xyz"})))
    (is (= false
           (nav/params-active? {:c 3 :b "xyz" :a 1} {:c 4})))
    (is (= false
           (nav/params-active? {:c 3 :b "xyz" :a 1} {:c 3 :b "abc"})))))

(deftest nav-link-test
  (testing "it creates a link"
    (is (= [:a {:href "?a=1&b=2", :class "button"} "name"]
           (nav/nav-link {:a 1} {:b 2} "name"))))
  (testing "it knows when a link is active"
    (is (= [:a {:href "?a=1&b=2", :class "button active"} "name"]
           (nav/nav-link {:a 1, :b 2} {:b 2} "name")))
    (is (= [:a {:href "?a=1&b=2", :class "button active"} "name"]
           (nav/nav-link {:a 1, :b 2} {:a 1} "name")))
    (is (= [:a {:href "?a=1&b=3", :class "button"} "name"]
           (nav/nav-link {:a 1, :b 2} {:b 3} "name")))
    (is (= [:a {:href "?a=1&b=3", :class "button"} "name"]
           (nav/nav-link {:a 1, :b 2} {:a 1, :b 3} "name")))))

(deftest page-link-test
  (testing "it creates a page link"
    (is (= [:a {:href "path?from=2", :class "button"} "name"]
           (nav/page-link :path "path" :is-active false :url-params {:from 2} :title "name"))))
  (testing "it knows when a link is active"
    (is (= [:a {:href "/?from=2", :class "button active"} "name"]
           (nav/page-link :path "/" :is-active true :url-params {:from 2} :title"name")))))
