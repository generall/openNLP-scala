package ml.generall.nlp

import org.scalatest.FunSuite

/**
  * Created by generall on 16.08.16.
  */
class OpenNLPChunkerTest extends FunSuite {

  test("testChunk") {
    val chunker = new OpenNLPChunker
    val res = chunker.chunk(List("I will go to Kirov on weekend")).head
    val kirov = res(4)
    assert(kirov._1 == "Kirov")
    assert(kirov._2._1 == "NNP")

    val trashTest = chunker.chunk(List("ololo burlululu 12312312321ekakjsdf alksjfalk @#$%^&*1927]]]")).head // Must not crash
    trashTest.foreach(println)
  }

  test("groupTest") {
    val chunker = new OpenNLPChunker
    val res = chunker.group(List("In two separate attacks, IRA bombs killed 18 British soldiers near Warrenpoint, and British admiral Louis Mountbatten and three others in County Sligo.")).head

    res.foreach(println)
  }

  test("listChunker") {
    val chunker = new OpenNLPChunker
    val res = chunker.chunk(List("I will go to Kirov", " city on weekend"))

    res.foreach(x => x.foreach(println))


  }

  test("testTitanicFilm") {
    val str = "James Cameron made Titanic"
    val chunker = new OpenNLPChunker
    val res = chunker.group(List(str))

    res.foreach(x => {
      x.foreach(println)
    })
  }

  test("testTitanicShip") {
    val str = "Edward Smith ruled Titanic"
    val chunker = new OpenNLPChunker
    val res = chunker.group(List(str))

    res.foreach(x => {
      x.foreach(println)
    })
  }


}
