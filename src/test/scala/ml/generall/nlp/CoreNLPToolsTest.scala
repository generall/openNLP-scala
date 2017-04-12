package ml.generall.nlp

import java.util.Properties

import edu.stanford.nlp.pipeline.StanfordCoreNLP
import org.scalatest.FunSuite

/**
  * Created by generall on 10.09.16.
  */
class CoreNLPToolsTest extends FunSuite {

  test("testProcess") {
    val nlp = new CoreNLPTools

    val tokens = nlp.process("Titanic hit iceberg in the Atlantic ocean")

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

  def time[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block    // call-by-name
    val t1 = System.currentTimeMillis()
    println("Elapsed time: " + (t1 - t0) + "ms")
    result
  }

  def runCoreNLP(params: String) = {
    val props = new Properties()
    props.setProperty("annotators", params) //"tokenize, ssplit, pos, lemma, ner, parse")
    val pipeline = new StanfordCoreNLP(props)

    time {
      pipeline.process("According to researcher Ibrahim Al-Khulaifi, there were three stages in the production of the show.")
      pipeline.process("The first stage was pre-production research, which identified basic education needs for children under the age of six.")
      pipeline.process("The second stage involved the creation of a pilot reel to test children on the show's effects and for review by educators, sociologists, psychologists, and other experts, who were invited to a seminar.")
      pipeline.process("Finally, the series was filmed and aired. Research began in August 1977; the team was led by an educator and included a linguist and a psychologist, all of whom were on the faculty of Kuwait University.")
      pipeline.process("The show was tested on different socioeconomic groups of children, between the ages of three and six, in kindergartens and preschools in four representative cities from Arab countries.")
      pipeline.process("The team proposed curriculum goals based upon the research and, during a seminar, Arab and CTW educators agreed on ten final goals.")
      pipeline.process("Iftah Ya Simsim emphasized scientific thinking and the effects of technology on society.")
      pipeline.process("It sought to provide children with experiences that enriched their knowledge about their environment and improved their reasoning, through teaching them mathematical and geometric concepts.")
      pipeline.process("The show introduced its viewers to Arab history by citing important events, such as showing castles that were the center of historic battles.")
      pipeline.process("Geography was highlighted, especially the location of countries and their cities and capitals, which had the secondary effect of helping children increase their feelings of belonging and feeling proud of their Arab heritage.")
    }

  }

  test("testSpeed") {
    runCoreNLP("tokenize, ssplit, pos, lemma, ner, parse")
  }


}
