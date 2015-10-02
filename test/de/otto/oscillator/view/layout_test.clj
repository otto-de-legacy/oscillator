(ns de.otto.oscillator.view.layout-test
  (:require [clojure.test :refer :all]
            [de.otto.oscillator.view.layout :as layout]
            [de.otto.oscillator.view.navigation :as link]))

(def page-navigation #'layout/page-navigation)

(deftest page-navigation-test
  (let [pages [{:name "MONGO" :url "/mongo"}
               {:name "JVM" :url "/jvm"}]]
    (with-redefs [link/page-link (fn [& {:keys [title is-active]}]
                                   (str title "-" (when is-active "active-") "page-link"))]
      (testing "it builds hiccup markup for each page"
        (is (= [:ul {:class "page"}
                (list [:li "MONGO-page-link"]
                      [:li "JVM-page-link"])]
               (page-navigation "" pages "/" {}))))
      (testing "it marks active page link"
        (is (= [:ul {:class "page"}
                (list [:li "MONGO-page-link"]
                      [:li "JVM-active-page-link"])]
               (page-navigation "" pages "/jvm" {})))))))
