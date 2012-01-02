package samples

import models._
import com.orientechnologies.orient.core.id.ORecordId
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.db.`object`.{ ODatabaseObject, ODatabaseObjectPool, ODatabaseObjectTx }
import com.orientechnologies.orient.core.Orient
import com.orientechnologies.orient.client.remote.OEngineRemote

object Samples {
  implicit def dbWrapper(db: ODatabaseObjectTx) = new {
    def queryBySql[T](sql: String, params: AnyRef*): List[T] = {
      val params4java = params.toArray
      val results: java.util.List[T] = db.query(new OSQLSynchQuery[T](sql), params4java: _*)
      results.asScala.toList
    }
  }
  
  def createDbObject(uri: String): ODatabaseObjectTx = {
    Orient.instance().registerEngine(new OEngineRemote());
    
    val db = new ODatabaseObjectTx(uri)
    
    // exists isn't supported for 'remote'?
//    if (!db.exists) {
//      db.create()
//    } else {
//      db.open("admin", "admin")
//    }
     db.open("admin", "admin")
  }

  def main(args: Array[String]) = {
    // ~~~~~~~~~~~~~ create db ~~~~~~~~~~~~~~~~~~~
	val db = createDbObject("remote:192.168.56.101/orientdbtest")

    // ~~~~~~~~~~~~ register models ~~~~~~~~~~~~~~~~
    db.getEntityManager.registerEntityClasses("models")

    // ~~~~~~~~~~~~~ create some data ~~~~~~~~~~~~~~~~
    var user: User = new User
    user.name = "aaa"
    db.save(user)

    var address1 = new Address
    address1.city = "NY"
    address1.street = "road1"
    var address2 = new Address
    address2.city = "ST"
    address2.street = "road2"

    user.addresses += address1
    user.addresses += address2
    db.save(user)

    var q1 = new Question
    q1.title = "How to use orientdb in scala?"
    q1.user = user
    db.save(q1)

    var q2 = new Question
    q2.title = "Show me a demo"
    q2.user = user
    db.save(q2)

    // ~~~~~~~~~~~~~~~~ count them ~~~~~~~~~~~~~~~~
    val userCount = db.countClass(classOf[User])
    println("User count: " + userCount)

    val questionCount = db.countClass(classOf[Question])
    println("Question count: " + questionCount)

    // ~~~~~~~~~~~~~~~~~ get all users ~~~~~~~~~~~~
    val users = db.queryBySql[User]("select from User")
    for (user <- users) {
      println(" - user: " + user)
    }

    // ~~~~~~~~~~~~~~~~~~ get the first user ~~~~~~~~
    val firstUser = db.queryBySql[User]("select from User limit 1").head
    println("First user: " + firstUser)

    // query by id
    val userById = db.queryBySql[User]("select from User where @rid = ?", new ORecordId(user.id))
    println("User by id: " + userById)

    // query by field
    val userByField = db.queryBySql[User]("select from User where name = ?", user.name)
    println("User by field: " + userByField)

    // query by city
    val userByCity = db.queryBySql[User]("select from User where addresses contains ( city = ? )", "NY")
    println("User by city: " + userByCity)

    // query questions of the user
    val questions = db.queryBySql[Question]("select from Question where user = ?", user)
    for (q <- questions) {
      println(" - question: " + q)
    }

    //db.delete()
    db.close()
  }
}