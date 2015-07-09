(ns monitor.axes
  (:require [dommy.core :refer-macros [sel sel1]]))

(defn format-number [num]
  (clojure.string/replace (str num)
                          #"(\d)(?=(\d{3})+(?!\d))"
                          "$1,"))

(defn set-axes [graph el]
  (Rickshaw.Graph.HoverDetail.
    (clj->js {:graph      graph
              :yFormatter (fn [y]
                            (if (nil? y)
                              y
                              (format-number y)))}))
  (Rickshaw.Graph.Axis.Time.
    (clj->js {:graph       graph
              :element     (sel1 el :.x_axis)
              :timeFixture (Rickshaw.Fixtures.Time.Local.)}))
  (Rickshaw.Graph.Axis.Y.
    (clj->js {:graph       graph
              :element     (sel1 el :.y_axis)
              :orientation "left"
              :tickFormat  Rickshaw.Fixtures.Number.formatKMBT}))
  (aset graph "axes-already-set" 1))