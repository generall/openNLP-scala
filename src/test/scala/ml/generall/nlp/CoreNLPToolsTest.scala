package ml.generall.nlp

import org.scalatest.FunSuite

/**
  * Created by generall on 10.09.16.
  */
class CoreNLPToolsTest extends FunSuite {

  test("testProcess") {
    val nlp = new CoreNLPTools

    val tokens = nlp.process("I will go to the Kirov city with Stalin on the weekend")

    tokens.foreach(println)
  }

  test("testPosition") {
    import Position._
    assert(Tools.checkRange((0, 1), (2, 3)) == lefter)
    assert(Tools.checkRange((0, 2), (2, 3)) == leftIntersect)
    assert(Tools.checkRange((0, 5), (2, 10)) == leftIntersect)
    assert(Tools.checkRange((0, 1), (0, 3)) == includeFirst)
    assert(Tools.checkRange((1, 3), (0, 3)) == includeFirst)
    assert(Tools.checkRange((1, 2), (0, 3)) == includeFirst)
    assert(Tools.checkRange((0, 3), (0, 1)) == includeSecond)
    assert(Tools.checkRange((0, 3), (1, 3)) == includeSecond)
    assert(Tools.checkRange((0, 3), (1, 2)) == includeSecond)
    assert(Tools.checkRange((0, 4), (-1, 3)) == rightIntersect)
    assert(Tools.checkRange((10, 20), (2, 3)) == righter)
  }

  test("testGetSentence") {
    val splitter = new SentenceSplitter
    val text = "Hello, my name is Isac Asimov. I am fantastic writer. I am good an science"

    val entRange1 = (36, 52)
    val entRange2 = (67, 74)

    val sentRagne = splitter.getSentence(text, entRange1).get
    val sentRagne2 = splitter.getSentence(text, entRange2).get

    val subSent1 = text.substring(sentRagne._1, sentRagne._2)
    val subSent2 = text.substring(sentRagne2._1, sentRagne2._2)
    assert(subSent1 == "I am fantastic writer.")
    assert(subSent2 == "I am good an science")

    val ent1 = subSent1.substring(entRange1._1 - sentRagne._1, entRange1._2 - sentRagne._1)
    val ent2 = subSent2.substring(entRange2._1 - sentRagne2._1, entRange2._2 - sentRagne2._1)

    println(ent1)
    println(ent2)

    println(text.substring(sentRagne._1, sentRagne._2))
    println(text.substring(sentRagne2._1, sentRagne2._2))

  }

}
