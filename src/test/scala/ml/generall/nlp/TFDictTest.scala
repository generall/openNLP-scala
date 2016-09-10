package ml.generall.nlp

import org.scalatest.FunSuite

/**
  * Created by generall on 10.09.16.
  */
class TFDictTest extends FunSuite {

  test("testGetCount") {
    val dict = new IDFDict

    val theWeight = dict.getIDF("the")
    val moveWeight = dict.getIDF("move")
    val colliderWeight = dict.getIDF("collide")
    val agrhWeight  = dict.getIDF("aaarrrrrgggggghhhhh")

    println(theWeight)
    println(moveWeight)
    println(colliderWeight)
    println(agrhWeight)

    assert(moveWeight < colliderWeight)
  }

}
