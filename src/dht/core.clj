(ns dht.core)


(def x 1)

(+ 1 x 2)




(require 'kibit.check)

(:alt (kibit.check/check-expr '(+ 1 x)))


