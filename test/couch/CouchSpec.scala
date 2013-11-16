import org.scalatest._
import org.scalamock.scalatest.MockFactory

import scala.concurrent.{Future, Await}
import scala.concurrent.duration._

import models._

class CouchSpec extends FlatSpec with ShouldMatchers with GivenWhenThen with MockFactory {
	val dbUrl = "http://localhost:5984/"
	val testDbName = "scala-couch-test"

	"Couch.addDb" should "succeed if that db has just been removed" in {
		Given("a couch instance")
		val couch = Couch.url(dbUrl)
		When("test db is removed")
		Await.result(couch.removeDb(testDbName), 1.second)
		And("test db is added")
		val result = Await.result(couch.addDb(testDbName), 1.second)
		Then("The response should be successful")
		result should be (true)
	}

	it should "fail if that db already exists" in {
		Given("a couch instance")
		val couch = Couch.url(dbUrl)
		When("test db is added")
		Await.result(couch.addDb(testDbName), 1.second)
		And("test db is added again")
		val result = Await.result(couch.addDb(testDbName), 1.second)
		Then("The response should be failure")
		result should be (false)
	}

	"Couch.removeDb" should "succeed if that db has just been added" in {
		Given("a couch instance")
		val couch = Couch.url(dbUrl)
		When("test db is added")
		Await.result(couch.addDb(testDbName), 1.second)
		And("test db is removed")
		val result = Await.result(couch.removeDb(testDbName), 1.second)
		Then("The response should be successful")
		result should be (true)
	}

	it should "fail if that db doesn't exist" in {
		Given("a couch instance")
		val couch = Couch.url(dbUrl)
		When("test db is not present")
		Await.result(couch.removeDb(testDbName), 1.second)
		And("test db is removed again")
		val result = Await.result(couch.removeDb(testDbName), 1.second)
		Then("The response should be failure")
		result should be (false)
	}
}