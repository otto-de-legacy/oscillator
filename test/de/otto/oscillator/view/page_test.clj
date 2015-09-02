(ns de.otto.oscillator.view.page-test
  (:require [clojure.test :refer :all]
            [de.otto.oscillator.view.page :as page]))

(deftest rendering-of-page-tiles
  (testing "should render html-fn"
    (let [a-tile-config {:type   :html-fn
                         :params (fn [] [:div "Some random HTML"])}]
      (is (= [:div "Some random HTML"]
             (#'page/build-tile a-tile-config nil nil nil)))))

  (testing "should render plain html"
    (let [a-tile-config {:type   :plain-html
                         :params [:div "Some random HTML"]}]
      (is (= [:div "Some random HTML"]
             (#'page/build-tile a-tile-config nil nil nil)))))

  (testing "should render an image"
    (let [a-tile-config {:type   :image
                         :params {:heading "somehead"
                                  :src "some-src"}}]
      (is (= [:div {:class "col image"}
              [:h2 "somehead"]
              [:img {:src "some-src"}]]
             (#'page/build-tile a-tile-config nil nil nil)))))

  (testing "should render a number"
    (let [a-tile-config {:type   :number
                         :params {:heading "somehead"
                                  :descr "some-descr"
                                  :num 123}}]
      (is (= [:div {:class "col number"}
              [:h2 "somehead"]
              [:div {:class "descr"} "some-descr"]
              [:div {:class "focus"} 123]]
             (#'page/build-tile a-tile-config nil nil nil))))))


(deftest evaluation-of-page-tiles
  (testing "should evalute html-fn with every request"
    (let [counter (atom 0)
          a-tile-config {:type   :html-fn
                         :params (fn [] [:div (str "Some random HTML - " @counter)])}]
      (is (= [:div "Some random HTML - 0"]
             (#'page/build-tile a-tile-config nil nil nil)))
      (swap! counter inc)
      (is (= [:div "Some random HTML - 1"]
             (#'page/build-tile a-tile-config nil nil nil)))))

  (testing "should evaluate plain-html only once"
    (let [counter (atom 0)
          a-tile-config {:type   :plain-html
                         :params [:div (str "Some random HTML - " @counter)]}]
      (is (= [:div "Some random HTML - 0"]
             (#'page/build-tile a-tile-config nil nil nil)))
      (swap! counter inc)
      (is (= [:div "Some random HTML - 0"]
             (#'page/build-tile a-tile-config nil nil nil))))))
