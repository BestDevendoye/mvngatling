package SapccIHM


import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder
import io.gatling.jdbc.Predef._

import scala.language.postfixOps


///////////////////////////////////////////////////
///	object :  HomePage 		            ///
///////////////////////////////////////////////////
object ObjectHomepage {


  private val tpsPaceDefault: Int = System.getProperty("tpsPace", "1000").toInt
  private val tpsPacingProducts: Int = System.getProperty("tpsPaceProducts", tpsPaceDefault.toString).toInt



  private val TpsPause: Int = System.getProperty("tpsPause", "10").toInt
  private val TempoMillisecond: Int = System.getProperty("TempoMillisecond", "10").toInt

  //private val NbreIter : Int = System.getProperty("nbIter", "5000").toInt
  private val NbreIterDefault: Int = System.getProperty("nbIter", "10").toInt
  private val NbreIter: Int = System.getProperty("nbIterProduct", NbreIterDefault.toString).toInt
  private val groupBy: String = System.getProperty("groupBy", "HomePage")

  //private val FichierPath: String = System.getProperty("dataDir", "./src/test/resources/data/")

  private val FichierPath: String = System.getProperty("dataDir", "")
  private val FichierDataProductId: String = "JddApiProductId.csv"

  val jddDataProductId = csv(FichierPath + FichierDataProductId).circular

  ///////////////////////////////////////////////////
  ///     scenario :      HomePage             ///
  ///////////////////////////////////////////////////

  val scnProductsId = scenario("ProductIds")

    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "HomePage") {
      exec { session => session.set("LeGroup", "HomePage") }
    } {
      exec { session => session.set("LeGroup", "HomePage") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "ProductId") }

        .feed(jddDataProductId)

        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/gl-fr/products/${productcode}")
            .header("Content-Type", "application/json")
            .check(status.is(200))
            .check(regex("errors").notExists.name("ProductId_Erreur"))
          )
            .exec {  session => println(session ) ; session }

        } // Fin group("${LeGroup}")
    } // Fin forever

  ///////////////////////////////////////////////////
  ///     scenario :      Pages              ///
  ///////////////////////////////////////////////////


  val scnPages = scenario("API_Pages")


    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "HomePage") {
      exec { session => session.set("LeGroup", "HomePage") }
    } {
      exec { session => session.set("LeGroup", "HomePage") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "Pages") }


        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/gl-fr/cms/pages")
            //    .header("Content-Type", "application/json")
            .check(status.is(200))
          )

            .exec {  session => println(session ) ; session }

        } // Fin group("${LeGroup}")
    } // Fin forever


  ///////////////////////////////////////////////////
  ///     scenario :       Product Components	             ///
  ///////////////////////////////////////////////////

  val scnComponents = scenario("API_Components")



    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "HomePage") {
      exec { session => session.set("LeGroup", "HomePage") }
    } {
      exec { session => session.set("LeGroup", "HomePage") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "Components") }


        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/gl-fr/cms/components")
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )
            //.pause ( TpsPause milliseconds)
            .exec {  session => println(session ) ; session }
        } // Fin group("${LeGroup}")
    } // Fin forever

val scnScenario = scenario("Test de perf ")


     exec(http("Get API")
     .get("/{baseSiteId}/products/search")
     .check(status.is(200)))


       val scnScenarioTest  = scenario("Test de perf gatling ")
     exec(http("POST API").get("/{baseSiteId}/products/search")
     .check(status.is(200)))

    val scnScenarioTest2  = scenario("Test de perf gatling2 ")
     exec(http("GET API").get("/{baseSiteId}/cms/components")
     .check(status.is(201)))




}
