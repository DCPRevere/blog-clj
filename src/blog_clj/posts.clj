(ns blog-clj.posts
  (:require [clojure.set :as set]
            [blog-clj.git :as git]))

(defn posts []
  (git/fetch-data))

(defn get-post [id]
  (first (filter #(= id (:id %)) (posts))))

(defn page [id]
  (let [post (get-post id)
        {:keys [title body date content]} post]
    [:div
     [:h3 title]
     [:h6 date]
     content]))

;; (defn tags []
;;   (sort
;;    (reduce (fn [set piece]
;;              (set/union set (:tags piece)))
;;            nil
;;            pieces)))

;; (defn page [id]
;;   (let [piece (first (filter #(= id (:id %)) pieces))
;;         {:keys [id name url logo desc creator]} piece]
;;     [:body
;;      [:div.jumobtron.vertical-center
;;       [:div.container
;;        [:a {:href url} [:h2 name]]
;;        [:a {:href url} url]
;;        [:p desc]
;;        (if creator
;;          [:p (str "Created by " creator)])]]]))
