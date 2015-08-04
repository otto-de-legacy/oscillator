(ns monitor.annotations
  (:require [ajax.core :as ajax]
            [dommy.core :refer-macros [sel sel1]]))

(def date-format
  {:hour "2-digit" :minute "2-digit" :year "numeric" :month "long" :day "numeric"})

(defn annotation-text [t target]
  (str (.toLocaleString (js/Date. (* t 1000)) "de-DE" (clj->js date-format))
       ":<br/>" target))

(defn dp->deployment [row]
  (->> (:datapoints row)
       (remove (fn [[v _]] (nil? v)))
       (map (fn [[_ t]]
              {:time  t
               :descr (annotation-text t (row :target))}))))

(defn handle-response [response annotator]
  (let [deployments (flatten (map dp->deployment response))]
    (doseq [d deployments]
      (.add annotator (d :time) (d :descr)))
    (.update annotator)))

(defn fetch-annotations [annotator url]
  (ajax/ajax-request
    {:uri             url
     :method          :get
     :handler         (fn [[ok response]]
                        (if ok
                          (handle-response response annotator)
                          (.error js/console (str "error fetching commits: " response))))
     :response-format (ajax/json-response-format {:keywords? true})}))

(defn set-annotator [graph el]
  (if-let [timeline-el (sel1 el :.timeline)]
    (let [annotator (Rickshaw.Graph.Annotate.
                      (clj->js {:graph   graph
                                :element timeline-el}))]
      (fetch-annotations annotator
                         (.getAttribute timeline-el "data-graphite-url")))))
