(ns quil-test.core
  (:use quil.core))


(def original-size [105 148])

(def scale-number 6)

(defn calculate-canvas-size []
  (vec (map (partial * scale-number) original-size)))

(def canvas-size (calculate-canvas-size))

(declare draw-circles
         draw-circle-one
         draw-circle-two
         draw-circle-three)

(defn key-pressed []
  (let [pressed-key (raw-key)]
    (case pressed-key
      \space (draw-circles)
      "default")))

(defn size-generator [min-s max-s]
  (fn [index]
    (random (* (* min-s 2) (canvas-size index))
            (* (* max-s 2) (canvas-size index)))))

(defn draw-circles []
  (background 255)
  (smooth)
  (stroke-weight 0)
  (fill 150 50)
  (draw-circle-one)
  (fill 75 50)
  (draw-circle-two)
  (fill 50 50)
  (draw-circle-three))

(defn draw-circle-one []
  (let [size-fn (size-generator 0.5 0.7)
        width   (size-fn 0)
        height  (size-fn 1)
        x (random (canvas-size 0))
        y (random (canvas-size 1))]
    (ellipse x
             y
             width
             height)))

(defn draw-circle-two []
  (let [size-fn (size-generator 0.1 0.3)
        width   (size-fn 0)
        height  (size-fn 1)
        x (random (canvas-size 0))
        y (random (canvas-size 1))]
    (ellipse x
             y
             width
             height)))

(defn draw-circle-three []
  (let [size-fn (size-generator 0.03 0.07)
        width   (size-fn 0)
        height  (size-fn 1)

        margin-fn (fn [index] (let [sz (canvas-size index)]
                                (/ (- sz (/ sz
                                            1.618))
                                   2)))
        margin-x  (margin-fn 0)
        margin-y  (margin-fn 1)

        x-max (- (canvas-size 0) margin-x)
        x (random margin-x x-max)

        y-max (- (canvas-size 1) margin-y)
        y (random margin-y y-max)]
    (ellipse x
             y
             width
             height)))

(defn setup []
  (draw-circles))

(defsketch composition
  :title "Composition"
  :setup setup
  :key-pressed key-pressed
  :size (calculate-canvas-size)
  :canvas-size canvas-size)
