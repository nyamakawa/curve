(ns curve.core
  (:require [curve.math :as math]
            [curve.bezier :as bezier]
            [curve.draw :as draw]))

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
    (draw/draw-line image data 30 30 200 200 [0 0 0])
    (draw/draw-line image data 30 100 200 200 [255 0 0])
    (draw/draw-line image data 30 150 200 200 [255 0 0])
    (draw/draw-line image data 30 200 200 200 [255 0 0])
    (draw/draw-line image data 10 10 10 200 [0 255 128])

    (let [draw-curve
          (fn [x1 y1 x2 y2 x3 y3 x4 y4]            
            (draw/draw-line image data x1 y1 x3 y3 [0 255 128])
            (draw/draw-line image data x2 y2 x4 y4 [0 255 128])
            (bezier/draw-curve
             x1 y1 x2 y2 x3 y3 x4 y4
             (fn [x1 y1 x2 y2]
               (draw/draw-line image data x1 y1 x2 y2 [0 0 255]))))]
      ;;(draw-curve 50 400 600 400 50 300 600 300)
      (draw-curve 100 500 300 300 100 400 200 300)

      (draw-curve 500 500 600 400 500 450 550 400)

      (draw-curve 400 200 600 200 400 100 600 100))

    (draw/draw-line image data 300 200 400 100 [255 0 0])

    (.putImageData ctx image 0 0)))
  
(draw (init))


(defn bench [name f]
  (.time js/console name)
  (f)
  (.timeEnd js/console name))
  
(bench "new"
       (fn []
         (dotimes [n 1000]
           (bezier/bezier 0.5 1.0 2.0 3.0 4.0))))

(bench "old"
       (fn []
         (dotimes [n 1000]
           (bezier/bezier-f 0.5 1.0 2.0 3.0 4.0))))

(bench "new"
       (fn []
         (dotimes [n 10000]
           (bezier/bezier 0.5 1.0 2.0 3.0 4.0))))

(bench "old"
       (fn []
         (dotimes [n 10000]
           (bezier/bezier-f 0.5 1.0 2.0 3.0 4.0))))
