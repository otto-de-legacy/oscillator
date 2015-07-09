(ns de.otto.oscillator.util.replacer
  (:require [clojure.string :as cs]))

(defn map-values [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn- pattern [env]
  (str "#{" (name env) "}"))

(defn- replace-env-pattern-using-rule [env res [pattern-name replace-rule]]
  (cs/replace res (pattern pattern-name) (replace-rule env)))

(defn- replace-envs-in-single-string [text params rules]
  (reduce (partial replace-env-pattern-using-rule (:env params)) text rules))

(defn- replace-envs-in-single-param's-values [chart rules node]
  ; we don't know the arity of the url params in advance
  (if (coll? node)
    (map #(replace-envs-in-single-string % chart rules) node)
    (replace-envs-in-single-string node chart rules)))

(defn replace-envs-in-params [replace-rules chart]
  (map-values (partial replace-envs-in-single-param's-values chart replace-rules) chart))
