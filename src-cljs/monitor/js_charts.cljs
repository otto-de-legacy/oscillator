(ns monitor.js-charts
  (:require [monitor.annotations :as annotations]
            [monitor.axes :as axes]
            [monitor.legend :as legend]
            [dommy.core :refer-macros [sel sel1]]))

(defn dp->coord [[value timestamp]]
  {:x timestamp :y value})

(defn transform-serie [serie]
  {:key  (serie :target)
   :data (map dp->coord (serie :datapoints))})

(defn transform-graphite-json [series]
  (->> (js->clj series :keywordize-keys true)
       (map transform-serie)
       (clj->js)))

(defn on-complete [el transport]
  (let [graph (aget transport "graph")]
    (when (not (= 1 (aget graph "axes-already-set")))
      (axes/set-axes graph el))
    (when (not (= 1 (aget graph "legend-already-set")))
      (legend/set-legend graph el))
    (annotations/set-annotator graph el)
    (.update graph)))

(defn colorize-data-serie [palette data-serie]
  (if (nil? (:color data-serie))
    (assoc data-serie :color (.color palette))
    data-serie))

(defn colorize-data-series [data-series-str]
  (let [palette (Rickshaw.Color.Palette. (clj->js {:scheme "spectrum14"}))
        data-series (.parse js/JSON data-series-str)]
    (->> (js->clj data-series :keywordize-keys true)
         (map (partial colorize-data-serie palette))
         (clj->js))))

(defn create-chart [el]
  (let [attrs {:element       (sel1 el :.chart)
               :renderer      "multi"
               :interpolation (.getAttribute el "data-interpolation")
               :stroke        true
               :max           (.getAttribute el "data-max")
               :height        (.getAttribute el "data-height")
               :dataURL       (.getAttribute el "data-url")
               :series        (colorize-data-series (.getAttribute el "data-series"))
               :onData        transform-graphite-json
               :onComplete    (partial on-complete el)}
        cleaned (into {} (remove (fn [[_ v]] (nil? v)) attrs))]
    (Rickshaw.Graph.Ajax. (clj->js cleaned))))

(defn refresh-chart [chart timeout]
  (js/setTimeout
    (fn []
      (.log js/console "refreshing after %ds" (/ timeout 1000))
      (.request chart)
      (refresh-chart chart timeout))
    timeout))

(defn on-document-ready []
  (doseq [el (sel :.chart_container)]
    (let [chart (create-chart el)]
      (refresh-chart chart (* 30 1000)))))

(.addEventListener js/document "DOMContentLoaded"
                   on-document-ready)



