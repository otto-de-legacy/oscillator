(ns de.otto.oscillator.graphite.dsl
  (:require [clojure.string :as cs]))

(defn aliaz [target name]
  (str "alias(" target ",\"" name "\")"))

(defn sum-series [target]
  (str "sumSeries(" target ")"))

(defn max-series [& targets]
  (str "maxSeries(" (cs/join "," targets) ")"))

(defn group [& targets]
  (str "group(" (cs/join "," targets) ")"))

(defn summarize [target, timespan]
  (str "summarize(" target ",\"" timespan "\",\"avg\")"))

(defn diff-series [target1, target2]
  (str "diffSeries(" target1 ", " target2 ")"))

(defn non-negative-derivative [target]
  (str "nonNegativeDerivative(" target ")"))

(defn average-series [target]
  (str "averageSeries(" target ")"))

(defn keep-last-value [target]
  (str "keepLastValue(" target ")"))

(defn divide-series [dividend-target divisor-target]
  (str "divideSeries(" dividend-target "," divisor-target ")"))

(defn scale [target factor]
  (str "scale("target "," factor ")"))

(defn scale-to-seconds [target seconds]
  (str "scaleToSeconds(" target "," seconds ")"))

(defn time-shift [target shift]
  (str "timeShift(" target "," shift ")"))
