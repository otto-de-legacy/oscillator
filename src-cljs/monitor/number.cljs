(ns monitor.number
  (:require
    [dommy.core :as d]
    [dommy.core :refer-macros [sel sel1]]))

(def jquery (js* "$"))

(defn first-datapoint [data]
  (-> (first data)
      (aget "datapoints")
      (ffirst)))

(defn formatter-string [el]
  (or (.getAttribute el "data-formatter") ".4s"))

(defn update-number [el data]
  (let [formatter (.format js/d3 (formatter-string el))]
    (->> (first-datapoint data)
         (formatter)
         (d/set-html! (sel1 el :.focus)))))

(defn fetch-and-update-number [el]
  (.get jquery (.getAttribute el "data-url")
        (partial update-number el)))

(defn refresh-number [number]
  (js/setInterval
    (fn []
      (fetch-and-update-number number))
    3000))

(defn on-document-ready []
  (doseq [el (sel :.col.number.target)]
    (refresh-number el)))

(.addEventListener js/document "DOMContentLoaded"
                   on-document-ready)
