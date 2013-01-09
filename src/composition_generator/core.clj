(ns composition-generator.core
  (:require [quil.core      :refer :all]
            [quil.applet    :refer [applet-exit current-applet]]
            [seesaw.chooser :refer [choose-file]]
            [seesaw.core    :refer [native!]])
  (:gen-class))

(def file-to-save (atom false))

(def original-size [105 148])

(def scale-number 6)

(defn calculate-canvas-size []
  (vec (map (partial * scale-number) original-size)))

(def canvas-size (calculate-canvas-size))

(defn save-to-file []
  (future (choose-file :filters [["Images" ["tiff" "targa" "png" "jpeg" "jpg"]]]
                       :type :save
                       :success-fn (fn [fc file] (reset! file-to-save (.getAbsolutePath file)))
                       :cancel-fn  (fn [fc file] (reset! file-to-save false)))))

(defn try-save-to-file []
  (when @file-to-save
    (save @file-to-save)
    (reset! file-to-save false)))

(declare draw-circles
         draw-circle-one
         draw-circle-two
         draw-circle-three)

(defn key-pressed []
  (let [pressed-key (raw-key)]
    (case pressed-key
      \space (draw-circles)
      \s     (save-to-file)
      \q     (applet-exit (current-applet))
      "default")))

(defn size-generator [min-s max-s]
  (fn [index]
    (random (* (* min-s 2) (canvas-size index))
            (* (* max-s 2) (canvas-size index)))))

(defn draw-circles []
  (background 200)
  (smooth)
  (stroke-weight 0)
  (fill 0)
  (draw-circle-one)
  (fill 100)
  (draw-circle-two)
  (fill 255)
  (draw-circle-three))

(defn draw-circle-at-any-canvas-coords [min-s max-s]
  (let [size-fn (size-generator min-s max-s)
        width   (size-fn 0)
        height  (size-fn 1)
        x (random (canvas-size 0))
        y (random (canvas-size 1))]
    (ellipse x
             y
             width
             height)))

(defn draw-circle-one []
  (draw-circle-at-any-canvas-coords 0.5 0.7))

(defn draw-circle-two []
  (draw-circle-at-any-canvas-coords 0.1 0.3))

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
  (native!)
  (draw-circles))

(defn draw []
  (try-save-to-file))

(defsketch composition-generator
  :title "Composition Generator"
  :setup setup
  :draw draw
  :key-pressed key-pressed
  :size (calculate-canvas-size)
  :canvas-size canvas-size)
