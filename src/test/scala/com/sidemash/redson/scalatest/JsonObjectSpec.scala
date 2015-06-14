package com.sidemash.redson.scalatest

import com.sidemash.redson.{JsonEntry, JsonNumber, JsonObject}

/**
 * Created by Serge Martial on 13/06/2015.
 */
class JsonObjectSpec extends UnitSpec {

  "A JsonObject" should "have registered values in first-in-first-out order at creation time" in {
    val array = JsonObject.of(JsonEntry.of("greet", "Hello"),JsonEntry.of("who", "World")) ;
    array.getHead() should be (JsonEntry.of("greet", "Hello"))
    array.getLast() should be (JsonEntry.of("who", "World"))
    array.getTail().getTail() should be (JsonObject.EMPTY)
  }


  it should "throw NoSuchElementException if the head of an empty JsonObject is accessed" in {
    a[NoSuchElementException] should be thrownBy {
      JsonObject.EMPTY.getHead()
    }
  }
  it should "throw UnsupportedOperationException if the tail of an empty JsonObject is accessed" in {
    a[UnsupportedOperationException] should be thrownBy {
      JsonObject.EMPTY.getTail()
    }
  }
  it should "throw NoSuchElementException if the last item of an empty JsonObject is accessed" in {
    a[NoSuchElementException] should be thrownBy {
      JsonObject.EMPTY.getLast()
    }
  }
}