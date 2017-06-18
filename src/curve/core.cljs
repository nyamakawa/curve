(ns curve.core
  (:require [curve.math :as math]))

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

(defn draw-pixel [image data x y r g b & a]
  (println "draw-pixel" x y r g b (first a))
  (let [offset (+ (* x 4) (* y (.-height image) 4))]
    (aset data offset r)
    (aset data (+ offset 1) g)
    (aset data (+ offset 2) b)
    (aset data (+ offset 3) (or (first a) 255)))
  data)

(defn draw-pixel-color [image data x y color]
  (let [length (count color)]
    (cond
      (= length 1)
      (draw-pixel image data x y
                  (nth color 0)
                  (nth color 0)
                  (nth color 0)
                  255)
      (= length 2)
      (draw-pixel image data x y
                  (nth color 0)
                  (nth color 0)
                  (nth color 0)
                  (nth color 1))
      (= length 3)
      (draw-pixel image data x y
                  (nth color 0)
                  (nth color 1)
                  (nth color 2)
                  255)
      (= length 4)
      (draw-pixel image data x y
                  (nth color 0)
                  (nth color 1)
                  (nth color 2)
                  (nth color 3))))
  data)

(defn draw-line-inner [image data x1 y1 x2 y2 dx dy error x y ystep color steep]
  (println "draw-line-inner" x1 y1 x2 y2 dx dy error x y ystep color steep)
  (if (<= x x2)
    (if steep
      (draw-pixel-color image data y x color)
      (draw-pixel-color image data x y color))
    (let [error (- error dy)]
      (if (< error 0)
        (recur image data x1 y1 x2 y2 dx dy (+ error dx) x (+ y ystep) ystep color steep)
        (recur image data x1 y1 x2 y2 dx dy error (+ x 1) y ystep color steep)))))

(defn draw-line [image data x1 y1 x2 y2 color]
  (let [steep (> (math/abs (- y2 y1)) (math/abs (- x2 x1)))]
    (if steep
      (recur image data y1 x1 y2 x2 color)
      (if (> x1 x2)
        (recur image data y2 x2 y1 x1 color)
        (let [dx (- x2 x1)
              dy (- y2 y1)
              error (/ dx 2)]
          (if (< y1 y2)
            (draw-line-inner image data x1 y1 x2 y2 dx dy error x1 y1 1 color steep)
            (draw-line-inner image data x1 y1 x2 y2 dx dy error x1 y1 -1 color steep)))))))

(defn draw [canvas]
  (let [ctx (.getContext canvas "2d")
        canvas-width (.-width canvas)
        canvas-height (.-height canvas)
        image (.createImageData ctx canvas-width canvas-height)
        data (.-data image)]
    ;(draw-pixel image data 0 0 0 0 0) ;test
    ;(draw-pixel-color image data 20 20 [255 0 0]) ;test
    (draw-line image data 30 30 200 200 [0 0 0])
    (.putImageData ctx image 0 0)))
  
(draw (init))
