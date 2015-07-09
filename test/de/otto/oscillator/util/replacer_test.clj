(ns de.otto.oscillator.util.replacer-test
  (:require [clojure.test :refer :all]
            [de.otto.oscillator.util.replacer :as replacer]))

(def replace-envs-in-single-param's-values #'replacer/replace-envs-in-single-param's-values)

(deftest replace-envs-in-single-value-test
  (let [rules {:identity (fn [env] env)
               :foo (fn [env] (str "rule-" (name env)))}]
    (testing "it does not hurt, when nothing is to be replaced"
      (is (= "example-string"
             (replace-envs-in-single-param's-values {:env "develop"} rules "example-string"))))
    (testing "replaces app-env placeholder"
      (is (= "example-rule-develop-string"
             (replace-envs-in-single-param's-values {:env "develop"} rules "example-#{foo}-string"))))
    (testing "replaces docker-env placeholder"
      (is (= "example-live-string"
             (replace-envs-in-single-param's-values {:env "live"} rules "example-#{identity}-string"))))
    (testing "it can handle vectors of values"
      (is (= ["example-rule-develop-string" "xyzdevelop123"]
             (replace-envs-in-single-param's-values {:env "develop"} rules ["example-#{foo}-string" "xyz#{identity}123"]))))))

(deftest replace-envs-in-params-test
  (let [rules {:identity (fn [env] env)
               :foo      (fn [env] (str "rule-" (name env)))}]
    (testing "replaces all env placeholders in each value of a map"
      (is (= {:env "develop" :a "example-rule-develop-string"}
             (replacer/replace-envs-in-params rules {:env "develop" :a "example-#{foo}-string"})))
      (is (= {:env "develop" :a "example-rule-develop-string" :b "xyzdevelop123"}
             (replacer/replace-envs-in-params rules {:env "develop" :a "example-#{foo}-string" :b "xyz#{identity}123"}))))
    (testing "it can handle vectors of values"
      (is (= {:env "develop" :a ["example-rule-develop-string" "xyzdevelop123"]}
             (replacer/replace-envs-in-params rules {:env "develop" :a ["example-#{foo}-string" "xyz#{identity}123"]}))))))
