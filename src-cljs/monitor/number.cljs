(ns monitor.number
  (:require
    [dommy.core :as d]
    [dommy.core :refer-macros [sel sel1]]))

(def jquery (js* "$"))

(defn update-number [el]
  (.get jquery (.getAttribute el "data-url")
        (fn [data]
          (->> (first (first (aget (first data) "datapoints")))
              (d/set-html! (first (sel el :.focus))))
          "json")))

(defn refresh-number [number]
  (js/setInterval
    (fn []
      (update-number number))
    3000))

(defn on-document-ready []
  (doseq [el (sel :.col.number.target)]
    (refresh-number el)))

(.addEventListener js/document "DOMContentLoaded"
                   on-document-ready)



