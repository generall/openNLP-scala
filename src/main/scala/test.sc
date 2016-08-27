val pattern = "ololo".r


val str = "test olol1o 123"

pattern.findFirstIn(str).isEmpty

val p = "[^-]+$".r

val tag = "I-NN"

p.findFirstIn(tag).get
