package ml.generall.nlp

import java.io._


/**
  * Created by generall on 10.09.16.
  */
class IDFDict {

  def readObjectFromFile[A](filename: String)(implicit m: scala.reflect.Manifest[A]): A = {
    val input = new ObjectInputStream(new FileInputStream(filename))
    val obj = input.readObject()
    obj match {
      case x if m.runtimeClass.isInstance(x) => x.asInstanceOf[A]
      case _ => sys.error("Type not what was expected when reading from file")
    }
  }

  def writeObjectToFile(obj: Object, filename: String) = {
    val oos = new ObjectOutputStream(new FileOutputStream(filename))
    oos.writeObject(obj)
    oos.close()
  }

  val totalWordCount = 22162602.0 /* from dict */


  val map: Map[String, List[Record]] = {
    val dict = "/tmp/lemma-hash.dat"
    if (new java.io.File(dict).exists) {
      readObjectFromFile[Map[String, List[Record]]](dict)
    } else {
      val tokenizerSteamModel: InputStream = getClass.getResourceAsStream("/ANC-all-lemma.txt")

      val res = scala.io.Source.fromInputStream(tokenizerSteamModel).getLines().map(str => {
        val arr = str.split("\t")
        val (word, lemma, pos, count) = (arr(0), arr(1), arr(2), arr(3))
        Record(word, lemma, pos, count.toInt)
      }).toList.groupBy(_.lemma)
      writeObjectToFile(res, dict)
      res
    }
  }

  def getIDF(lemma: String): Double = {
    Math.log( totalWordCount / map.getOrElse(lemma, List()).foldLeft(1)( _ + _.count ))
  }

}
