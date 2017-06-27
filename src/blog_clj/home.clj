(ns blog-clj.home
  (:require [blog-clj.posts :as posts]))

(defn page []
  (for [post (posts/posts)]
    (let [{:keys [id title date]} post]
      [:div.panel.panel-default
       [:div.panel-body
        [:a {:href (str "/posts/" id)}
         [:h3 title]]]
       [:div.panel-footer [:h6 date]]
       ])))
