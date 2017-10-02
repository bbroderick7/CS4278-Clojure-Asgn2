(ns engn-web.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [cljs-time.core :as time]
              [cljs-time.format :as time-format]
              [cljs-time.coerce :as time-coerce]
              [clojure.string :as string]
              [reagent-material-ui.core :as ui]
              [ajax.core :refer [GET POST]]
              [engn-web.local-messaging :as messaging]))


;; ==========================================================================
;; Utility functions
;;
;; These functions are particularly helpful if you try to use icons with
;; material UI
;; ==========================================================================

(enable-console-print!)

(def el reagent/as-element)
(defn icon-span [nme] [ui/FontIcon {:className "material-icons"} nme])
(defn icon [nme] (el [:i.material-icons nme]))
(defn color [nme] (aget ui/colors nme))
(defn alert [text] (js/alert text))

;; ==========================================================================
;; App State
;;
;; ==========================================================================

;; this is the initial state that you defined in the first
;; assignment as a map where each channel has a string key
;; and the keys are bound to a list of messages
(defonce msgs (atom (messaging/messages-initial-state)))
(defonce current-channel (atom "default"))

;; ==========================================================================
;; View components
;; ==========================================================================


;; Step 1.
;;
;; Fill in the message-component function to create a simple display
;; for the message that uses a Card and CardText to display the message.
;;
;; When you are done, the list of messages in the "default" channel should
;; be displayed on the page.
;;
;; Step 2.
;;
;; Add a CardHeader to show the name of the user
;; that posted the message.
;;
;; When you are done, each message should have a header that shows the user
;; that posted the message
;;
;; http://www.material-ui.com/#/components/card
(defn message-component [msg])

(defn messages-list-component [ms]
  [:div {:style {:margin-bottom "100px"}}
    (for [msg ms]
      ^{:key msg} [message-component msg])])


(defn main-page []
  ;; We will talk about the @ sign and what it does later.
  (let [msgs @msgs
        current-channel @current-channel
        current-msgs (messaging/messages-get msgs current-channel)
        channel-names (messaging/channels-list msgs)]
   [ui/MuiThemeProvider
      [:div
       ;; Step 3.
       ;;
       ;; Add a new bold text "Channels" before the "Messages" to delineate the start of the channels.
       ;;
       ;; Step 4.
       ;;
       ;; Add a new "channels-list-component" to display the list of channel names as
       ;; a List before the messages.
       ;;
       ;; Step 5.
       ;;
       ;; Add a click-handler to your channel names so that clicking on a channel name resets
       ;; the "current-channel" atom, with the channel name that was clicked and causes the
       ;; list of messages from that channel to be displayed.
       ;;
       [:b "Messages"]
       [messages-list-component current-msgs]]]))





;; -------------------------
;; Routes
;;
;; Don't touch anything down here yet! We will
;; talk about this later in class.

(def page (atom #'main-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'main-page))


;; -------------------------
;; Initialize app
;;
;; Don't touch anything down here yet! We will
;; talk about this later in class.

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
