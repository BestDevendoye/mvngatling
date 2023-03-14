package SapccIHM
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import scala.language.postfixOps

///////////////////////////////////////////////////
///     scenario :      Panier             ///
///////////////////////////////////////////////////

object ObjectPanierPage {
  private val tpsPaceDefault: Int = System.getProperty("tpsPace", "1000").toInt
  private val tpsPacingProducts: Int = System.getProperty("tpsPaceProducts", tpsPaceDefault.toString).toInt


  private val TpsPause: Int = System.getProperty("tpsPause", "10").toInt
  private val TempoMillisecond: Int = System.getProperty("TempoMillisecond", "10").toInt

  //private val NbreIter : Int = System.getProperty("nbIter", "5000").toInt
  private val NbreIterDefault: Int = System.getProperty("nbIter", "1").toInt
  private val NbreIter: Int = System.getProperty("nbIterProduct", NbreIterDefault.toString).toInt

  private val groupBy: String = System.getProperty("groupBy", "HomePage")

 //private val FichierPath: String = System.getProperty("dataDir", "./src/test/resources/data/")
 private val FichierPath: String = System.getProperty("dataDir", "")
  private val FichierDataProductId: String = "JddApiProductId.csv"

  val jddDataProductId = csv(FichierPath + FichierDataProductId).circular



  ///////////////////////////////////////////////////
  ///     scenario :       API CART RECO 	             ///
  ///////////////////////////////////////////////////

  val scnCartRECO = scenario("API_CART_RECO")

    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PanierPage") {
      exec { session => session.set("LeGroup", "PanierPage") }
    } {
      exec { session => session.set("LeGroup", "PanierPage") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "Api_cart_reco") }


        .group("${LeGroup}") {
          exec(http("${L_API}")
            .post("/gl-fr/t2s/getreco?lang=fr&curr=EUR")
            .header("Content-Type", "application/json")
            .asJson
            .body(StringBody(
              """
                |{
                |    "pageId": "1201",
                |    "spaceId": "1201-85",
                |    "sessionId": "d8a8f6fe-78bf-421f-9d8a-9eb321d8252a"
                |}
                |""".stripMargin)).asJson
            .check(status.is(200))
            .check(regex("errors").notExists.name("catalogs_Erreur"))
          )

            //.pause ( TpsPause milliseconds)


            //////////////////////////////////////////////////////
            ///     suivant la présence des Erreurs : trace
            //////////////////////////////////////////////////////
            .doIf(session => session.attributes.contains("LesCodesErreurs")) {
              foreach("${LesCodesErreurs}", "UnCode") {
                exec { session => println("[PERF_MSG] Pour L_API " + session("L_API").as[String] + " , Erreur = " + session("UnCode").as[String]); session }
                  .pause(TempoMillisecond milliseconds)
              }
                .exec(session => session.remove("LesMessagesErreurs"))
            }

        } // Fin group("${LeGroup}")
    } // Fin forever



  ///////////////////////////////////////////////////
  ///     scenario :      Pages              ///
  ///////////////////////////////////////////////////


  val scnPages = scenario("API_Pages")


    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PanierPage") {
      exec { session => session.set("LeGroup", "PanierPage") }
    } {
      exec { session => session.set("LeGroup", "PanierPage") }
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

            .exec { session => println(session); session }

        } // Fin group("${LeGroup}")
    } // Fin forever


  ///////////////////////////////////////////////////
  ///     scenario :       Product Components	             ///
  ///////////////////////////////////////////////////

  val scnComponents = scenario("API_Components")


    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PanierPage") {
      exec { session => session.set("LeGroup", "PanierPage") }
    } {
      exec { session => session.set("LeGroup", "PanierPage") }
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
            .exec { session => println(session); session }
        } // Fin group("${LeGroup}")
    } // Fin forever

  ///////////////////////////////////////////////////
  ///     scenario :       Product Components	             ///
  ///////////////////////////////////////////////////

  val scnCardLocality  = scenario("API_CardLocality")


    .exec { session => session.set("detail", groupBy) }
    .doIfEqualsOrElse(session => session("detail").as[String], "PanierPage") {
      exec { session => session.set("LeGroup", "PanierPage") }
    } {
      exec { session => session.set("LeGroup", "PanierPage") }
    }

    .repeat(NbreIter) { //.forever {
      exec(flushSessionCookies)
        .exec(flushHttpCache)
        .exec(flushCookieJar)

        .pace(tpsPacingProducts milliseconds)

        .exec { session => session.set("L_API", "API_CardLocality") }


        .group("${LeGroup}") {
          exec(http("${L_API}")
            .get("/users/lilahemdane+9@gmail.com/carts/00001010/loyalty")
            .header("Content-Type", "application/json")
            .check(status.is(200))
          )
            //.pause ( TpsPause milliseconds)
            .exec { session => println(session); session }
        } // Fin group("${LeGroup}")
    } // Fin forever
}
