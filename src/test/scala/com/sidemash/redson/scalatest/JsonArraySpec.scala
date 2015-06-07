package com.sidemash.redson.scalatest

import com.sidemash.redson.{JsonNumber, JsonArray}

class JsonArraySpec extends UnitSpec {

  "A JsonArray" should "have registered values in last-in-first-out order at creation time" in {
    val array = JsonArray.of(Integer.valueOf(1), Integer.valueOf(2)) ;
    array.getHead() should be (JsonNumber.of(1))
    array.getLast().asInt() should be (2)
    array.getTail().getHead().asInt() should be (2)
  }


  val emptyJsArr = JsonArray.EMPTY
  it should "throw NoSuchElementException if the head of an empty JsonArray is accessed" in {
    a[NoSuchElementException] should be thrownBy {
        emptyJsArr.getHead()
    }
  }
  it should "throw NoSuchElementException if the tail of an empty JsonArray is accessed" in {
    a[NoSuchElementException] should be thrownBy {
      emptyJsArr.getTail()
    }
  }
  it should "throw NoSuchElementException if the last items of an empty JsonArray is accessed" in {
    a[NoSuchElementException] should be thrownBy {
      emptyJsArr.getLast()
    }
  }
}