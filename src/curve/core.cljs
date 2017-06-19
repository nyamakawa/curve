(ns curve.core
  (:require [curve.math :as math]
            curve.draw))

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


(defn draw [canvas]
  (let [ctx (.getContext canvas "2d")
        canvas-width (.-width canvas)
        canvas-height (.-height canvas)
        image (.createImageData ctx canvas-width canvas-height)
        data (.-data image)]
    ;(draw-pixel image data 0 0 0 0 0) ;test
    ;(run! (fn [x] (draw-pixel-color image data 0 x [255 0 0])) (range 30 600 1))
    ;(draw-pixel-color image data 200 200 [255 0 0]) ;test
    (curve.draw/draw-line image data 30 30 200 200 [0 0 0])
    (curve.draw/draw-line image data 30 100 200 200 [255 0 0])
    (curve.draw/draw-line image data 30 150 200 200 [255 0 0])
    (curve.draw/draw-line image data 30 200 200 200 [255 0 0])
    (.putImageData ctx image 0 0)))
  
(draw (init))
