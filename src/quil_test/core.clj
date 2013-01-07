(ns quil-test.core
  (:use quil.core))


(def canvas-size [525 740])

(def number 10)

(declare draw-circles)

(defn key-pressed []
  (let [pressed-key (raw-key)]
    (case pressed-key
      \space (draw-circles)
      "default")))

(defn draw-circles []
  (let [size-fn #(random (* 1 (canvas-size %))
                         (* 1.4 (canvas-size %)))
        width (size-fn 0)
        height (size-fn 1)
        x (random (canvas-size 0))
        y (random (canvas-size 1))]
    (ellipse x
             y
             width
             height)))

(defn setup []
  (background 255)
  (smooth)
  (stroke-weight 1)
  (fill 150 50)
  (draw-circles))

(defsketch gen-art-30
  :title "Composition"
  :setup setup
  :key-pressed key-pressed
  :canvas-size canvas-size)
