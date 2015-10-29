(ns monitor.axes
  (:require [dommy.core :refer-macros [sel sel1]]
            [goog.i18n.DateTimeFormat :as dtf]
            [goog.i18n.NumberFormat :as nf]))

(defn format-number [num]
  (let [fmt (goog.i18n.NumberFormat. goog.i18n.NumberFormat.Format.DECIMAL)]
    (.format fmt num)))

(defn format-time [datetime]
  (let [fmt (goog.i18n.DateTimeFormat. "HH:mm:ss dd.MM.yyyy")]
    (.format fmt (js/Date. (* datetime 1000)))))

(defn set-axes [graph el]
  (Rickshaw.Graph.HoverDetail.
    (clj->js {:graph      graph
              :yFormatter (fn [y] (when y (format-number y)))
              :xFormatter (fn [x] (when x (format-time x)))}))
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