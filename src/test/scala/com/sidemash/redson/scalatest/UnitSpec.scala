package com.sidemash.redson.scalatest

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
abstract class UnitSpec extends AnyFlatSpec
  with Matchers with OptionValues with Inside with Inspectors