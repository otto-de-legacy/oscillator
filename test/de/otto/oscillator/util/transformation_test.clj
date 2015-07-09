(ns de.otto.oscillator.util.transformation-test
  (:require [clojure.test :refer :all]
            [de.otto.oscillator.util.transformation :as transf]))

(def add-alias-to-targets #'transf/add-alias-to-targets)
(def as-data-series #'transf/as-data-series)
(def scale-targets #'transf/scale-targets)

(deftest transform-test
  (testing "it consolidates the targets and gives them an alias"
    (is (= {:abc    "xyz"
            :target ["alias(avg-series(abc),\"key\")"]}
           (add-alias-to-targets {:abc     "xyz"
                                  :targets {:key {:target "avg-series(abc)"}}})))

    (is (= {:abc    "xyz"
            :target ["alias(avg-series(abc),\"key1\")"
                     "alias(sum-series(def),\"key2\")"]}
           (add-alias-to-targets {:abc     "xyz"
                                  :targets {:key1 {:target "avg-series(abc)"}
                                            :key2 {:target "sum-series(def)"}}})))))

(deftest data-series-test
  (testing "it transforms the chart-definition into a rickshaw series-definition"
    (let [chart-def {:abc     "xyz"
                     :targets {:my-key1 {:target "avg-series(abc)"
                                         :color  "green"}
                               :my-key2 {:target "sum-series(def)"
                                         :color  "blue"}}}]
      (is (= [{:key "my-key1" :name "my-key1" :renderer "line" :color "green"}
              {:key "my-key2" :name "my-key2" :renderer "line" :color "blue"}]
             (as-data-series chart-def)))))
  (testing "it sets the name for each series"
    (let [chart-def {:abc     "xyz"
                     :targets {:my-key1 {:target "avg-series(abc)"
                                         :name   "My Name"
                                         :color  "green"}
                               :my-key2 {:target "sum-series(def)"
                                         :color  "blue"
                                         }}}]
      (is (= [{:key "my-key1" :name "My Name" :renderer "line" :color "green"}
              {:key "my-key2" :name "my-key2" :renderer "line" :color "blue"}]
             (as-data-series chart-def)))))
  (testing "it sets the rendere for each series"
    (let [chart-def {:abc     "xyz"
                     :targets {:my-key1 {:target   "avg-series(abc)"
                                         :renderer "stack"
                                         :color    "green"}
                               :my-key2 {:target "sum-series(def)"
                                         :color  "blue"}}}]
      (is (= [{:key "my-key1" :name "my-key1" :renderer "stack" :color "green"}
              {:key "my-key2" :name "my-key2" :renderer "line" :color "blue"}]
             (as-data-series chart-def))))))

(deftest scale-targets-test
  (testing "it should annotate each target with a summarize function"
    (is (= {:yMax    5000
            :targets {:key1 {:target "summarize(abc,\"1hour\",\"avg\")" :renderer "stack"}
                      :key2 {:target "summarize(def,\"1hour\",\"avg\")"}}}
           (scale-targets {:yMax    5000
                           :targets {:key1 {:target "abc" :renderer "stack"}
                                     :key2 {:target "def"}}} "1hour")))))