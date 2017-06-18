(ns curve.core
  (:require ))

(enable-console-print!)

(println "This text is printed from src/curve/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

(def canvas-dom )

(println (.-innerText (.getElementById js/document "header1")))


(defn init []
  (let [canvas-dom (.getElementById js/document "main-canvas")]
    (set! (.-width canvas-dom) 800)
    (set! (.-height canvas-dom) 600)    
    canvas-dom))

(defn draw-pixel [canvas data x y r g b & a]
  (let [offset (+ (* x 4) (* y (.-height canvas) 4))]
    (aset data offset r)
    (aset data (+ offset 1) g)
    (aset data (+ offset 2) b)
    (aset data (+ offset 3) (or a 255)))
  data)

(defn draw [canvas]
  (let [ctx (.getContext canvas "2d")
        canvas-width (.-width canvas)
        canvas-height (.-height canvas)
        image (.createImageData ctx canvas-width canvas-height)
        data (.-data image)]
    (draw-pixel canvas data 0 0 0 0 0) ;test
    (.putImageData ctx image 0 0)))
  
(draw (init))
