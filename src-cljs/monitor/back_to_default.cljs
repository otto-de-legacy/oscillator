(ns monitor.back-to-default)

(defn go-back-to-default-page-after [timeout]
  (.log js/console "going back to defaults after %d mins" (/ timeout 60 1000))
  (js/setTimeout #(aset js/document "location" "href" "/") timeout))

(go-back-to-default-page-after (* 10 60 1000))
