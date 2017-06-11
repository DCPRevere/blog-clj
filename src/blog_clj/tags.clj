(ns blog-clj.tags
  (:require [blog-clj.piece :as piece]))

(defn has-tag [piece tag]
  (let [{:keys [tags]} piece]
   (some #(= tag %) tags)))

(defn page [tag]
  [:body
   [:div.container
    [:div.jumbotron.text-center
     [:h1 "Nomad Expo"]
     [:h4 "tag: " tag]]
    (let [pieces (filter #(has-tag % tag) piece/pieces)]
      (for [piece (sort-by :promoted?
                           #(- (compare % %2))
                           pieces)]
        (let [{:keys [id name url logo desc creator promoted?]} piece]
          [:div.panel.panel-default
           [:div.panel-body {:class (if promoted? "promoted")}
            [:a {:href (str "/piece/" id)} [:h2 name]]
            [:p {:align "right"} desc]]])))]])
