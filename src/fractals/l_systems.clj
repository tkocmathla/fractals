(ns fractals.l-systems
  (:require
    [quil.core :as q]
    [quil.middleware :as qm]))

(def systems
  [; quadratic koch island
   {:n 5 :start-x 500 :start-y 450 :angle 90 :len -5 :sentence "F-F-F-F" :rules {\F "F-F+F+FF-F-F+F"}}
   ; quadratic snowflake curve
   {:n 6 :start-x 700 :start-y 500 :angle 90 :len -8 :sentence "-F" :rules {\F "F+F-F-F+F"}}
   ; pentagons of pentagons
   {:n 6 :start-x 25 :start-y 350 :angle 72 :len -8 :sentence "F-F-F-F-F" :rules {\F "F-F++F+F-F-F"}}

   ; islands and lakes
   {:n 4 :start-x 150 :start-y 450 :angle 90 :len -8 :sentence "F+F+F+F" :rules {\F "F+f-FF+F+FF+Ff+FF-f+FF-F-FF-Ff-FFF" \f "ffffff"}}

   ; sierpinski gasket with edge rewriting
   {:n 8 :start-x 300 :start-y 500 :angle 60 :len -7 :sentence "r" :rules {\l "r+l+r" \r "l-r-l"}}
   ; hexagonal gosper curve with edge rewriting
   {:n 6 :start-x 150 :start-y 500 :angle 60 :len -10 :sentence "r" :rules {\l "l+r++r-l--ll-r+" \r "-l+rr++r+l--l-r"}}

   ; tree with node rewriting
   {:n 6 :start-x 350 :start-y 600 :angle 22.5 :len -10 :sentence "F" :rules {\F "FF+[+F-F-F]-[-F+F+F]"}}
   ; tree 2
   {:n 9 :start-x 350 :start-y 600 :angle 20 :len -2 :sentence "f" :rules {\f "F[+f]F[-f]+f" \F "FF"}}
   ; tree 3
   {:n 9 :start-x 350 :start-y 600 :angle 25.7 :len -2 :sentence "f" :rules {\f "F[+f][-f]Ff" \F "FF"}}
   ; tree 4
   {:n 10 :start-x 350 :start-y 600 :angle 25.7 :len -1 :sentence "f" :rules {\f "F-[[f]+f]+F[+Ff]-f" \F "FF"}}])

(def system (get systems 0))

(def commands
  {\F #(do (q/line 0 0 0 (:len system)) (q/translate 0 (:len system)))
   \f #(q/translate 0 (:len system))
   \+ #(q/rotate (* (:angle system) (/ Math/PI 180)))
   \- #(q/rotate (* (:angle system) (/ Math/PI 180) -1))
   \[ #(q/push-matrix)
   \] #(q/pop-matrix)
   ; copies of F for edge rewriting
   \l #(do (q/line 0 0 0 (:len system)) (q/translate 0 (:len system)))
   \r #(do (q/line 0 0 0 (:len system)) (q/translate 0 (:len system)))})

(defn setup []
  (q/frame-rate 2)
  (:sentence system))

(defn step [sentence]
  (if (< (q/frame-count) (:n system))
    (apply str (replace (:rules system) sentence))
    sentence))

(defn draw [sentence]
  (q/background 255)
  (q/translate (:start-x system) (:start-y system))
  ((apply juxt (map commands sentence))))

(q/defsketch l-systems
  :title "L-Systems"
  :size [700 600]
  :setup setup
  :draw draw
  :update step
  :middleware [qm/fun-mode])
