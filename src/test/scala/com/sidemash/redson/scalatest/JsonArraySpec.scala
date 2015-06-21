package com.sidemash.redson.scalatest

import java.util.function.Predicate

import com.sidemash.redson._

class JsonArraySpec extends UnitSpec {

  "A JsonArray" should "have registered values in first-in-first-out order at creation time" in {
    val array = JsonArray.of(Integer.valueOf(1), Integer.valueOf(2)) ;
    array.getHead should be (JsonNumber.of(1))
    array.getLast.asInt() should be (2)
    array.getTail.getHead.asInt() should be (2)
  }


  it should "throw NoSuchElementException if the head of an empty JsonArray is accessed" in {
    a[NoSuchElementException] should be thrownBy {
      JsonArray.EMPTY.getHead
    }
  }

  it should "throw UnsupportedOperationException if the tail of an empty JsonArray is accessed" in {
    a[UnsupportedOperationException] should be thrownBy {
      JsonArray.EMPTY.getTail
    }
  }

  it should "throw NoSuchElementException if the last item of an empty JsonArray is accessed" in {
    a[NoSuchElementException] should be thrownBy {
      JsonArray.EMPTY.getLast
    }
  }

  it should "updateWhere should replace all even number in array" in {
    val array = JsonArray.of(
                  Integer.valueOf(1),
                  Integer.valueOf(2),
                  Integer.valueOf(3),
                  Integer.valueOf(4),
                  Integer.valueOf(5),
                  Integer.valueOf(6))
    val predicate = new Predicate[JsonEntry[Integer]] {
      override def test(t: JsonEntry[Integer]): Boolean = t.getValue.asInt() % 2 == 0
    }
    array.updateWhere(predicate, JsonNumber.of(64)) should be(
      JsonArray.of(
        Integer.valueOf(1),
        Integer.valueOf(64),
        Integer.valueOf(3),
        Integer.valueOf(64),
        Integer.valueOf(5),
        Integer.valueOf(64)
      )
    )
  }

  it should "updateWhile should update all number less than 3" in {
    val array = JsonArray.of(
      Integer.valueOf(0),
      Integer.valueOf(1),
      Integer.valueOf(2),
      Integer.valueOf(3),
      Integer.valueOf(4),
      Integer.valueOf(5),
      Integer.valueOf(6)
    )
    val predicate = new Predicate[JsonEntry[Integer]] {
      override def test(t: JsonEntry[Integer]): Boolean = t.getValue.asInt() < 3
    }
    array.updateWhile(predicate, JsonNumber.of(64)) should be(
      JsonArray.of(
        Integer.valueOf(64),
        Integer.valueOf(64),
        Integer.valueOf(64),
        Integer.valueOf(3),
        Integer.valueOf(4),
        Integer.valueOf(5),
        Integer.valueOf(6)
      )
    )
  }

  it should "updateFirst should replace first value in array" in {
    val array = JsonArray.of(Integer.valueOf(1), Integer.valueOf(2))
    val predicate =  new Predicate[JsonEntry[Integer]] {
      override def test(t: JsonEntry[Integer]): Boolean = t.getValue.asInt() < 3
    }
    array.updateFirst(predicate, JsonNumber.of(64)) should be (
      JsonArray.of(
        Integer.valueOf(64),
        Integer.valueOf(2)
      )
    )
  }
}