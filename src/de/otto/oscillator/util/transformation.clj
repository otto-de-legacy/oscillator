(ns de.otto.oscillator.util.transformation
  (:require [de.otto.oscillator.graphite.dsl :as dsl]
            [clojure.data.json :as json]))

(defn map-values [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn- scale-single-target [resolution target-params]
  (let [target (target-params :target)]
    (assoc-in target-params [:target] (dsl/summarize target resolution))))

(defn- scale-targets [chart resolution]
  (let [scaled-targets (map-values (partial scale-single-target resolution) (:targets chart))]
    (assoc-in chart [:targets] scaled-targets)))

(defn- add-alias-to-target [[key target]]
  (dsl/aliaz (:target target) (name key)))

(defn- add-alias-to-targets [chart]
  (-> chart
      (dissoc :targets)
      (assoc :target (vec (map add-alias-to-target (:targets chart))))))

(defn free-ymax-if-needed [chart-def ymax-mode]
  (if (= ymax-mode "free")
    (dissoc chart-def :yMax)
    chart-def))

(defn- as-data-series [chart-def]
  (vec (map (fn [[k v]] {:key      (name k)
                         :name     (:name v (name k))
                         :color    (:color v)
                         :renderer (:renderer v "line")}) (:targets chart-def))))

(defn transformed-chart [chart-def resolution ymax-mode]
  (-> chart-def
      (free-ymax-if-needed ymax-mode)
      (scale-targets resolution)
      (add-alias-to-targets)))

(defn data-series [chart-def]
  (json/write-str (as-data-series chart-def)))
