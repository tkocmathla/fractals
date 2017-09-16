(ns fractals.cantor
  (:require
    [quil.core :as q]
    [quil.middleware :as qm]))

(def y-pad 50)

(defn cantor [{len :len :as line}]
  (when (>= len 1)
    [(-> line (update :y + y-pad) (update :len / 3))
     (-> line (update :x + (* len 2/3)) (update :y + y-pad) (update :len / 3))]))

(defn setup []
  (q/frame-rate 2)
  (q/fill 0)
  {:lines [{:x (- (/ (q/width) 2) 450) :y y-pad :len 900}]})

(defn step [{:keys [lines]}]
  {:lines (flatten (keep cantor lines))})

(defn draw [{:keys [lines]}]
  (doseq [{:keys [x y len]} lines]
    (q/rect x y len 20)))

(q/defsketch cantor-set
  :title "Cantor Set"
  :size [1000 500]
  :setup setup
  :update step
  :draw draw
  :middleware [qm/fun-mode])
