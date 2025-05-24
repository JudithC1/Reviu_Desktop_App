package com.example.reviu_app.API

import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class ApiService() : CoroutineScope {
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

//    var urlApi = "http://172.16.24.149:45455/"
    var urlApi = "http://10.46.1.115:45455/"

    val client = HttpClient(CIO) {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(Logging){
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
    }


    // authentification
    suspend fun getAuthentificationUsuari(correu: String, contrasenya: String): Usuari? {
        return try {
            val response = client.get(urlApi+"api/Authentifications/"+correu+"&"+contrasenya)
            if (response.status  == HttpStatusCode.OK) {
                response.body<Usuari>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun postAuthentification(Authentification: Authentification): Authentification? {
        print(Authentification.toString())
        return try {
            val response = client.post(urlApi+"api/Authentifications/"){
                contentType(ContentType.Application.Json)
                setBody(Authentification)
            }
            if (response.status  == HttpStatusCode.OK) {
                return response.body<Authentification>()
            } else {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    // usuari
    suspend fun getUsuari(id : Int) : Usuari? {
        return try {
            val response = client.get(urlApi+"api/Usuaris/" + id)
            if (response.status  == HttpStatusCode.OK) {
                response.body<Usuari>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getUsuariNom(nom: String) : List<Usuari>? {
        return try {
            val response = client.get(urlApi+"api/Usuaris/" + nom)
            if (response.status  == HttpStatusCode.OK) {
                response.body<List<Usuari>>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun putUsuari(usuari: Usuari): HttpResponse{
        return client.put(urlApi + "api/Usuaris/" + usuari.usuariId){
            contentType(ContentType.Application.Json)
            setBody(usuari)
        }
    }

    suspend fun postFoto(bytes: ByteArray, id: Int): foto? {
        var afegit: foto? = null
        val requestFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", "foto.jpg", requestFile)
        val userId = id.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val response: HttpResponse = client.submitFormWithBinaryData(
//            url = "http://172.16.24.149:45455/api/Usuaris/uploadImage",
            url = urlApi+ "api/Usuaris/uploadImage",
            formData = formData {
                append("image", bytes , io.ktor.http.Headers.build {
                    append(io.ktor.http.HttpHeaders.ContentType, "image/jpeg")
                    append(io.ktor.http.HttpHeaders.ContentDisposition, "filename=\"foto.jpg\"")
                })
                append("userId", id.toString())
            }
        )
        return if (response.status.isSuccess()) {
            response.body<foto>()
        } else {
            null
        }
    }

    suspend fun getAllUsuaris() : List<Usuari>? {
        return try {
            val response = client.get(urlApi+"api/Usuaris/")
            if (response.status  == HttpStatusCode.OK) {
                response.body<List<Usuari>>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }


    // respostes
    suspend fun getRespostes(id : Int): List<Comentari>?{
        return try {
            val response = client.get(urlApi+"api/Respostes/" + id)
            if (response.status  == HttpStatusCode.OK) {
                response.body<List<Comentari>>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    // comentaris
    suspend fun getComentaris(type : Char, id : Int): List<Comentari>?{
        return try {
            val response = client.get(urlApi+"api/Comentaris/" + type + "/" + id)
            if (response.status  == HttpStatusCode.OK) {
                response.body<List<Comentari>>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun postComentari(Comentari: Comentari): Comentari? {
        return try {
            val response = client.post(urlApi+"api/Comentaris/"){
                contentType(ContentType.Application.Json)
                setBody(Comentari)
            }
            if (response.status  == HttpStatusCode.OK) {
                response.body<Comentari>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun putComentaris(comentari: Comentari): HttpResponse{
        return client.put(urlApi + "api/Comentaris/" + comentari.comentariId){
            contentType(ContentType.Application.Json)
            setBody(comentari)
        }
    }


    // llistes
    suspend fun postLlistes(lliste: Lliste): Lliste?{
        return try {
            val response = client.post(urlApi+"api/Llistes/"){
                contentType(ContentType.Application.Json)
                setBody(lliste)
            }
            if (response.status  == HttpStatusCode.OK) {
                response.body<Lliste>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteLlista(llistaId : Int ): HttpResponse {
        return client.delete(urlApi + "api/Llistes/" + llistaId)
    }

    suspend fun postCuntingutLlistes(cuntingutLliste: CuntigutLliste): CuntigutLliste?{
        println(cuntingutLliste.toString())
        return try {
            val response = client.post(urlApi+"api/CuntigutLlistes/"){
                contentType(ContentType.Application.Json)
                setBody(cuntingutLliste)
            }
            if (response.status  == HttpStatusCode.OK) {
                response.body<CuntigutLliste>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getLlistesUsuari(id : Int) : List<Lliste>? {
        return try {
            val response = client.get(urlApi+"api/LlistesUsuari/" + id)
            if (response.status  == HttpStatusCode.OK) {
                response.body<List<Lliste>>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    suspend fun deleteContingutLlista(contingutLlistaId : Int ): HttpResponse {
        return client.delete(urlApi + "api/CuntigutLlistes/" + contingutLlistaId)
    }

    suspend fun getLliste(id : Int) : Lliste?{
        return try {
            val response = client.get(urlApi+"api/Lliste/" + id)
            if (response.status  == HttpStatusCode.OK) {
                response.body<Lliste>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    suspend fun getSeason(season : Int, id : Int) : season?{
        return try {
            val response = client.get(urlApi+"api/season/" + id + "/" + season)
            if (response.status  == HttpStatusCode.OK) {
                response.body<season>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {

        }
    }


    // contingut
     suspend fun getContingutBD( id : Int) : Contingut?{
        return try {
            val response = client.get(urlApi+"api/Continguts/" + id)
            if (response.status  == HttpStatusCode.OK) {
                response.body<Contingut>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getContingutTitol(title : String) : buscarContingutPerNom?{
        return try {
            val response = client.get(urlApi+"api/Continguts/" + title)
            if (response.status  == HttpStatusCode.OK) {
                response.body<buscarContingutPerNom>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    suspend fun getContingutDTO(type : String, id : Int) : ContingutDTO?{
        return try {
            val response = client.get(urlApi+"api/ContingutsDTO/" + type + "/" + id)
            if (response.status  == HttpStatusCode.OK) {
                response.body<ContingutDTO>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    suspend fun getContingutDBTMDB(id : Int, type : String) : Contingut?{
        return try {
            val response = client.get(urlApi+"api/ContingutsDBTMDB/" + id + "/" + type)
            if (response.status  == HttpStatusCode.OK) {
                response.body<Contingut>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    // proveidors
    suspend fun getProveidors(id : Int, tipus: String) : proveidors?{
        return try {
            val response = client.get(urlApi+"api/proveidors/" + id + "/" + tipus )
            if (response.status  == HttpStatusCode.OK) {
                response.body<proveidors>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    suspend fun getPopular() : resultatsRecomanacions?{
        return try {
            val response = client.get(urlApi+"api/Popular/")
            if (response.status  == HttpStatusCode.OK) {
                response.body<resultatsRecomanacions>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    // recomendation
    suspend fun getRecomendation(type : String, id : Int) : resultatsRecomanacions?{
        return try {
            val response = client.get(urlApi+"api/recomendation/" + type + "/" + id)
            if (response.status  == HttpStatusCode.OK) {
                response.body<resultatsRecomanacions>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    // ultimsllancamanets
    suspend fun getUltimsLlencaments() : resultatsLlancaments?{
        return try {
            val response = client.get(urlApi+"api/ultimsLlancaments/")
            if (response.status  == HttpStatusCode.OK) {
                response.body<resultatsLlancaments>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    suspend fun getProximsLlencaments() : resultatsLlancaments?{
        return try {
            val response = client.get(urlApi+"api/proximsLlancaments/")
            if (response.status  == HttpStatusCode.OK) {
                response.body<resultatsLlancaments>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }


    // seguiment
    suspend fun postSeguiment(seguiment: Seguiment): Seguiment?{
        return try {
            val response = client.post(urlApi+"api/Seguiments/"){
                contentType(ContentType.Application.Json)
                setBody(seguiment)
            }
            if (response.status  == HttpStatusCode.OK) {
                response.body<Seguiment>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    suspend fun getSeguiments(idSeguit : Int, idSegueix: Int ) : Seguiment? {
        return try {
            val response = client.get(urlApi+"api/Seguiments/" + idSeguit + "&" + idSegueix)
            if (response.status  == HttpStatusCode.OK) {
                response.body<Seguiment>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    suspend fun deleteSeguiments(idSeguit : Int, idSegueix: Int ): HttpResponse {
        return client.delete(urlApi + "api/Seguiments/" + idSeguit + "&" + idSegueix )
    }


    // valoracions
    suspend fun postValoracions(valoracio: Valoracio) : Valoracio?{
        return try {
            val response = client.post(urlApi+"api/Valoracios/"){
                contentType(ContentType.Application.Json)
                setBody(valoracio)
            }
            if (response.status  == HttpStatusCode.OK) {
                response.body<Valoracio>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {

        }
    }

    suspend fun getValoracions(id: Int, data: String) : List<Valoracio>?{
        println(id.toString()+""+data)
        return try {
            val response = client.get(urlApi+"api/Valoracios/" + id + "/" + data)
            if (response.status  == HttpStatusCode.OK) {
                response.body<List<Valoracio>?>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun putValoracions(valoracio: Valoracio): HttpResponse{
        return client.put(urlApi + "api/Valoracios/" + valoracio.valoracioId){
            contentType(ContentType.Application.Json)
            setBody(valoracio)
        }
    }

    suspend fun getValoracionsPropies(id : Int) : List<Valoracio>?{
        return try {
            val response = client.get(urlApi+"api/Valoracios/" + id )
            if (response.status  == HttpStatusCode.OK) {
                response.body<List<Valoracio>>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

    suspend fun getValoracionsContingut(id : Int) : List<Valoracio>?{
        return try {
            val response = client.get(urlApi+"api/ValoraciosContingut/" + id)
            if (response.status  == HttpStatusCode.OK) {
                response.body<List<Valoracio>>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
        }
    }

}
