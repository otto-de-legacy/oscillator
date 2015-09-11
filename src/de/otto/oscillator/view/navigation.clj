(ns de.otto.oscillator.view.navigation
  (:require [de.otto.oscillator.util.graphite-url :as url]))

(defn params-active? [current-params link-params]
  (every? (fn [[k, v]] (= v (k current-params))) link-params))

(defn nav-link-url [current-params new-params]
  (str "?" (url/param-string (merge current-params new-params))))

(defn time-display [title]
  [:div {:class "button active"}
   [:div {:class "vertical"} title]])

(defn nav-link [current-params new-params title]
  [:a {:href  (nav-link-url current-params new-params)
       :class (if (params-active? current-params new-params) "button active" "button")} title])

(defn- broaden? [side]
  (if (= side :from) neg? pos?))

(defn nav-link-scroll-button [current-params side delta title]
  [:a {:href  (nav-link-url current-params {side (url/move-time (side current-params) delta)})
       :class "button scroll"
       :title (str (if ((broaden? side) delta) "BROADEN" "NARROW") " BY " delta "h")} title])

(defn nav-link-vertical-button [current-params new-params title]
  [:a {:href  (nav-link-url current-params new-params)
       :class "button time"
       :title (str "JUMP TO " title)}
   [:div {:class "vertical"} title]])

(defn page-link [& {:keys [path is-active url-params title]}]
  (let [current-params (select-keys url-params [:resolution :env :from :until :ymax-mode])]
    [:a {:href  (str path (nav-link-url current-params {}))
         :class (if is-active "button active" "button")} title]))
