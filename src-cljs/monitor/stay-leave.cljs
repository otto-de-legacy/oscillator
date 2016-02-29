(ns monitor.stay-leave
  (:require [dommy.core :as dommy :refer-macros [sel sel1]]))

(defn on-document-ready []
  (when (some-> (sel1 "nav.top ul.stay a.button.active")
                (dommy/text)
                (= "LEAVE"))
    (let [timeout-in-min 5]
      (.log js/console "going back to defaults after %d mins" timeout-in-min)
      (js/setTimeout #(aset js/document "location" "href" "/")
                     (* 60 1000 timeout-in-min)))))

(.addEventListener js/document "DOMContentLoaded"
                   on-document-ready)
