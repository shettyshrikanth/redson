package com.sidemash.redson.scalatest

import java.util.function.UnaryOperator

import com.sidemash.redson.{JsonEntry, JsonObject, JsonString}


class JsonObjectSpec extends UnitSpec {

  "A JsonObject" should "have registered values in first-in-first-out order at creation time" in {
    val array =
      JsonObject.of(
        JsonEntry.of("greet", "Hello"),
        JsonEntry.of("who", "World")
      );
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
  it should "change updated keys when all combinations of updateKey call succeed" in {
    val obj =
      JsonObject.of(
        JsonEntry.of("greet", "Hello"),
        JsonEntry.of("who", "World")
      )
    val uppercaseOperator = new UnaryOperator[String] {
      override def apply(t: String): String = t.toUpperCase
    }

    obj.updateKey("greet", "GREET") should be (JsonObject.of(JsonEntry.of("GREET", "Hello"),JsonEntry.of("who", "World")))
    obj.updateKey("greet", "GREET").getHead() should be  (JsonEntry.of("GREET", "Hello"))

    obj.updateKey("greet", JsonString.of("Hello"), "GREET") should be (JsonObject.of(JsonEntry.of("GREET", "Hello"),JsonEntry.of("who", "World")))
    obj.updateKey("greet", JsonString.of("Hello"),"GREET").getHead() should be  (JsonEntry.of("GREET", "Hello"))

    obj.updateKey("greet", uppercaseOperator) should be (JsonObject.of(JsonEntry.of("GREET", "Hello"),JsonEntry.of("who", "World")))
    obj.updateKey("greet", uppercaseOperator).getHead() should be  (JsonEntry.of("GREET", "Hello"))
  }

  it should "never update keys when all combinations of updateKey when no update is needed" in {
    val obj =
      JsonObject.of(
        JsonEntry.of("greet", "Hello"),
        JsonEntry.of("who", "World")
      )
    val uppercaseOperator = new UnaryOperator[String] {
      override def apply(t: String): String = t.toUpperCase
    }


    JsonObject.EMPTY.updateKey("reet", "GREET") should be (JsonObject.EMPTY)
    JsonObject.EMPTY.updateKey("", JsonString.of("Hello"), "GREET") should be (JsonObject.EMPTY)
    JsonObject.EMPTY.updateKey("grt", uppercaseOperator) should be (JsonObject.EMPTY)

    obj.updateKey("", "GREET") should be (obj)
    obj.updateKey("geet", "GREET").getHead() should be  (JsonEntry.of("greet", "Hello"))

    obj.updateKey("", JsonString.of("Hello"), "GREET") should be (obj)
    obj.updateKey("", JsonString.of("Hello"),"GREET").getHead() should be  (JsonEntry.of("greet", "Hello"))

    obj.updateKey("", uppercaseOperator) should be (obj)
    obj.updateKey("gree", uppercaseOperator).getHead() should be  (JsonEntry.of("greet", "Hello"))
  }
}