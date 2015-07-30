(ns de.otto.oscillator.util.graphite-url
  (:require [de.otto.oscillator.util.replacer :as r]))

(defn single-param-tuple [k v]
  (str (name k) "=" v))

(defn param-tuples [params]
  (flatten
    (for [[k v] params]
      (if (or (seq? v) (vector? v))
        (map #(single-param-tuple k %) v)
        (single-param-tuple k v)))))

(defn param-string [params]
  (apply str (interpose "&" (param-tuples params))))

(defn build-url [page-config chart additional-params]
  (let [escaped-params (r/replace-envs-in-params (:replace-rules page-config) chart)]
    (str (:base-url page-config) "render/?" (-> escaped-params
                                                (select-keys [:target :from :until])
                                                (merge additional-params)
                                                (param-string)))))

(defn json-url [page-config chart]
  (build-url page-config chart {:format "json"}))

(defn image-url [page-config chart]
  (build-url page-config chart {:width 1024 :height 762}))

(defn parse-hours [current-str]
  (let [str-to-parse (or current-str "")
        parsed-num (re-find #"-?\d+(?=h)" str-to-parse)]
    (read-string (or parsed-num "0"))))

(defn move-time [current-str f operand]
  (let [current-hours (parse-hours current-str)
        result (f current-hours operand)]
    (if (> result -1)
      "-1min"
      (str result "h"))))
