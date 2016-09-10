package ml.generall.nlp

import org.scalatest.FunSuite

/**
  * Created by generall on 10.09.16.
  */
class CoreNLPToolsTest extends FunSuite {

  test("testProcess") {
    val nlp = new CoreNLPTools

    nlp.process("I will go to the Kirov city with Stalin on the weekend")
  }

}
