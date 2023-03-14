package SapccIHM
import com.redis.serialization.Parse

import scala.concurrent.duration._
import sys.process._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import scala.language.postfixOps

class MDC_APISeq  extends  Simulation{

  private val host: String = System.getProperty("urlCible", "https://sapapi-rec.galerieslafayette.com/occ/v2")
  private val VersionAppli: String = System.getProperty("VersionApp", "Vxx.xx.xx")
  private val TpsMonteEnCharge: Int = System.getProperty("tpsMonte", "4").toInt
  private val TpsPalier: Int = System.getProperty("tpsPalier", (2 * TpsMonteEnCharge).toString).toInt
  private val TpsPause: Int = System.getProperty("tpsPause", "60").toInt
  private val DureeMax: Int = System.getProperty("dureeMax", "1").toInt + 5 * (TpsMonteEnCharge + TpsPalier)

  private val LeCoeff: Int = System.getProperty("coeff", "10").toInt
  private val PourcentConnect: Int = System.getProperty("pourcentageConnecte", "10").toInt


  private val LesVUNonConnect: Int = System.getProperty("nbVUNonConnect", "1").toInt
  private val LesHomepage: Int = System.getProperty("nbHomepage", LesVUNonConnect.toString).toInt
  private  val LesProduct: Int = System.getProperty("nbrpoduct", LesVUNonConnect.toString).toInt
  private val  nbVu : Int =  LeCoeff * 1


  val httpProtocol =   http.baseUrl(host)
  //   .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
  // .acceptEncodingHeader("gzip, deflate")
  //.acceptLanguageHeader("fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3")
  //.userAgentHeader("TESTS-DE-PERF")
  //.silentResources
  before {

    println("----------------------------------------------" )
    println("host :"+ host   )
    println("VersionAppli :"+ VersionAppli   )
    println("TpsPause : " + TpsPause  )
    println("LeCoeff : " + LeCoeff  )
    println("nbVu : " + nbVu  )
    println("tpsMonte : " + TpsMonteEnCharge )
    println("----------------------------------------------" )
  }

  after  {
    println("----------------------------------------------" )
    println("--------     Rappel - Rappel - Rappel    -----" )
    println("VersionAppli :"+ VersionAppli   )
    println("host :"+ host   )
    println("TpsPause : " + TpsPause  )
    println("LeCoeff : " + LeCoeff  )
    println("nbVu : " + nbVu  )
    println("DureeMax : " + DureeMax )
    println("tpsMonte : " + TpsMonteEnCharge )
    println("--------     Rappel - Rappel - Rappel    -----" )
    println("----------------------------------------------" )
    println(" " )
    // println( CommandAfter  )
  }


  val ProductsId = scenario("ProductIds").exec(ObjectHomepage.scnProductsId)
  val ApiPages = scenario("API_PAGES").exec(ObjectHomepage.scnPages)
  val APIConponents = scenario("Components").exec(ObjectHomepage.scnComponents)
  val PPorductid = scenario("PProducts").exec(ObjectFicheProduct.scnProductsId)
  val PApiPages = scenario("PApiPages").exec(ObjectFicheProduct.scnPages)
  val PApiComponents = scenario("PApicomponements").exec(ObjectFicheProduct.scnComponents)
  val PApigetreco  = scenario("PApigetreco").exec(ObjectFicheProduct.scnCartRECO)
  val PApireviews = scenario("PApireviews").exec(ObjectFicheProduct.scnProductReviews)
  val panierApigetreco = scenario("Panier Apigetreco ").exec(ObjectPanierPage.scnCartRECO)
  val panierApiComponents = scenario("panier ApiComponents").exec(ObjectPanierPage.scnComponents)
  val panierApiPages = scenario("panier ApiPages").exec(ObjectPanierPage.scnPages)
  val panierCardLocality = scenario("panier CardLocality").exec(ObjectPanierPage.scnCardLocality)
  val pageApiComponents = scenario("page ApiComponents").exec(ObjectPageList.scnComponents)
  val pagepageid = scenario("page pageid").exec(ObjectPageList.scnPages)
  val pageproducid = scenario("page producrid").exec(ObjectPageList.scnProductsId)
  val pagesearch  = scenario("page search").exec(ObjectPageList.scnProductSearch)

  setUp(

  /* ProductsId.inject(rampUsers(nbVu * 2 * LesHomepage) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    ApiPages.inject(rampUsers(nbVu * 2 * LesHomepage) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    APIConponents.inject(rampUsers(nbVu * 2 * LesHomepage) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    PPorductid.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    PApiPages.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    PApiComponents.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    PApigetreco.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    PApireviews.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    panierApigetreco.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    panierApiComponents.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    panierApiPages.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    panierCardLocality.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes))*/
    pageApiComponents.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    pagepageid.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    pageproducid.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes)),
    pagesearch.inject(rampUsers(nbVu * 2 * LesProduct) during ( TpsMonteEnCharge  minutes) , nothingFor(  TpsPalier  minutes))

  ).protocols(httpProtocol)
    .maxDuration( DureeMax minutes)

}
