(ns monitor.legend
  (:require [dommy.core :refer-macros [sel sel1]]))

(defn set-legend [graph el]
  (let [legend-el (sel1 el :.legend)]
    (when legend-el
      (let [legend (Rickshaw.Graph.Legend.
                     (clj->js {:graph   graph
                               :element legend-el}))]
        (Rickshaw.Graph.Behavior.Series.Toggle.
          (clj->js {:graph  graph
                    :legend legend}))
        (Rickshaw.Graph.Behavior.Series.Highlight.
          (clj->js {:graph         graph
                    :legend        legend
                    :disabledColor (fn [c] "#233240")})))))
  (aset graph "legend-already-set" 1))
