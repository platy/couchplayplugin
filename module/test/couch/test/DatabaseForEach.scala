package couch.test

import couch.CouchDatabase

import scala.concurrent.{Future, Await, ExecutionContext}
import scala.concurrent.duration._
import org.scalatest.{BeforeAndAfterEach, Suite}

/** Add to a scalatest suite to initialise a fresh couchDB database on a conifgured server for each test
  * The db is created when the testDb is first requested and is deleted after the test finishes
  */
trait DatabaseForEach extends BeforeAndAfterEach with TestCouchHost { self: Suite =>
  implicit private val executor: ExecutionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext

  private val testDbName = testCouchConfig.database

  private var initialised = false
  private var created = false
  
  /** Returns a CouchDatabase api object for interfacing with a test database,
    * this database will be deleted when the test finishes.
    */ 
  def testDb: CouchDatabase = {
    if(!initialised)
      throw new IllegalStateException("testDb requested before test method is started - this may be that it is being used in another before block. Use\n  override def beforeWithDatabase()\nfor any setup you want to do with the database")
    if(!created) {
      Await.result(couchHost.addDb(testDbName), 1.second)
      created = true
    }
    couchHost.db(testDbName)
  }

  /** Provides a CouchDatabase api object for a non-existant database */
  val absentDb = couchHost.db("non-existent-db")

  override def beforeEach() {
    created = false
    initialised = true
    beforeWithDatabase()
    super.beforeEach() // To be stackable, must call super.beforeEach
  }

  override def afterEach() {
    try {
      super.afterEach() // To be stackable, must call super.afterEach
    } finally {
      Await.result(couchHost.removeDb(testDbName), 1.second)
      initialised = false
      created = false
    }
  }

  /** Override with any code to be run before the test which must run after the database is initialised */
  def beforeWithDatabase() = ()
}
