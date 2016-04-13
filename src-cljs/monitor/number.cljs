(ns monitor.number
  (:require
    [dommy.core :as d]
    [dommy.core :refer-macros [sel sel1]]))

(def jquery (js* "$"))

(defn first-datapoint [data]
  (-> (first data)
      (aget "datapoints")
      (last)
      (first)))

(defn formatter-string [el]
  (or (.getAttribute el "data-formatter") ".4s"))

(defn update-number [el data]
  (let [formatter (.format js/d3 (formatter-string el))]
    (->> (first-datapoint data)
         (formatter)
         (d/set-html! (sel1 el :.focus)))))

(defn fetch-and-update-number [elements]
  (doseq [el elements]
    (.get jquery (.getAttribute el "data-url")
          (partial update-number el))))

(defn refresh-number [elements]
  (fetch-and-update-number elements)
  (js/setInterval
    (fn []
      (fetch-and-update-number elements))
    3000))

(defn on-document-ready []
  (refresh-number (sel :.col.number.target)))

(.addEventListener js/document "DOMContentLoaded"
                   on-document-ready)
