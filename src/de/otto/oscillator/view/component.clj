(ns de.otto.oscillator.view.component
  (:require [de.otto.oscillator.util.graphite-url :as url]
            [de.otto.oscillator.util.transformation :as transf]))

(defn chart-definition [chart-def url-params]
  (merge (transf/transformed-chart chart-def
                                   (url-params :resolution "10mins")
                                   (url-params :ymax-mode)) url-params))

(defn plain-graphite-link [page-config chart-def url-params]
  (let [chart-def (chart-definition chart-def url-params)
        image-url (url/image-url page-config chart-def)]
    [:a {:href image-url :class "graphite-image-url"} image-url]))

(defn rickshaw-svg [page-config chart-def annotation-event-targets url-params]
  (let [transformed-chart-def (chart-definition chart-def url-params)]
    [:div {:class              "chart_container"
           :data-url           (url/json-url page-config transformed-chart-def)
           :data-min           (transformed-chart-def :yMin)
           :data-max           (transformed-chart-def :yMax)
           :data-interpolation (transformed-chart-def :interpolation "cardinal")
           :data-height        (if (url-params :detail) 750 200)
           :data-series        (transf/data-series chart-def)}
     [:div {:class "y_axis"}]
     [:div {:class "chart"}]
     [:div {:class "x_axis"}]
     (if (and (url-params :detail) annotation-event-targets)
       [:div {:class             "timeline"
              :data-graphite-url (url/json-url page-config {:target annotation-event-targets
                                                            :env    (:env url-params)})}]
       )
     (if (url-params :detail)
       [:div {:class "legend"}])]))

(defn link-to-chart [page-config chart-def chart-name url-params]
  [:div {:class "col"}
   [:h2 chart-name]
   [:a {:href (str "/detail?" (url/param-string (merge url-params {:detail true
                                                                   :chart  (name chart-name)})))}
    (rickshaw-svg page-config chart-def :no-annotation url-params)]])

(defn image [{:keys [heading src]}]
  [:div {:class "col image"}
   [:h2 heading]
   [:img {:src src}]])

(defn number [{:keys [heading descr num]}]
  [:div {:class "col number"}
   [:h2 heading]
   [:div {:class "descr"} descr]
   [:div {:class "focus"} num]])
