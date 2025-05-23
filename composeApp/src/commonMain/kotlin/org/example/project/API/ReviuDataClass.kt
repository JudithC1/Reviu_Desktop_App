package com.example.reviu_app.API

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Authentification(
    var authentificationId: Int?,
    var correu : String,
    var contrasenya : String,
    var fkUsariId : Int,
    var fkUsari : Usuari,
)

@Serializable
data class Usuari(
    var usuariId : Int?,
    var nomUsuari : String,
    var fotoUsuari : String?,
    var seguidors : Int,
    var seguits : Int,
    var fkContingutId : Int?,
    var comentaris : List<Comentari>?,
    var fkContingut : Contingut?,
    var llistes : List<Lliste>?,
//    var valoracios : List<Valoracio>?,
)

@Serializable
data class foto(
    var imageUrl: String
)

@Serializable
data class Comentari(
    var comentariId : Int?,
    var likesComentari : Int,
    var dataPublicacioComentari : String, //Date,
    var esResposta : Int?,
    var fkUsuariId : Int,
    var fkContingutId: Int?,
    var fkValoracioId : Int?,
    var fkContingut: Contingut?,
    var fkUsuari: Usuari?,
    var fkValoracio : Valoracio?,
    var textComentari : String,
)

@Serializable
data class Contingut(
    var contingutId : Int?,
    var valoracio : Double?,
    var tmdbId : Int?,
    var tipus : String,
    var comentaris: List<Comentari>,
//    var contingutLlistes: List<CuntigutLliste>,
//    var usuaris : List<Usuari>,
//    var valoracios: List<Valoracio>,
    var poster_path: String?
)

@Serializable
data class Lliste(
    var llistaId : Int?,
    var nomLlista : String,
    var fotoLlista : List<Byte>?,
    var esPublica : Boolean,
    var fkUsuariId: Int,
    var cuntigutLlistes : List<CuntigutLliste>
)

@Serializable
data class Valoracio(
    var valoracioId : Int?,
    var puntuacio : Int,
    var likesValoracio : Int,
    var dataPublicacioValoracio: String,
    var fkUsuariId: Int,
    var fkContingutId: Int,
    var comentaris : List<Comentari>?,
    var fkContingut: Contingut?,
    var fkUsuari: Usuari?,
)

@Serializable
data class CuntigutLliste(
    var contingutLlistaId : Int?,
    var fkLlistaId : Int,
    var fkContingutId: Int,
    var fkContingut: Contingut?,
)

@Serializable
data class buscarContingutPerNom(
    var page : Int,
    var results : List<resultatBusquedaNom>,
    var total_pages : Int,
    var total_results : Int,
)

@Serializable
data class resultatBusquedaNom(
    var adult : Boolean?,
    var backdrop_path : String?,
    var id : Int,
    var title : String?,
    var original_title : String?,
    var name : String?,
    var original_name : String?,
    var overview : String?,
    var poster_path : String?,
    var media_type : String,
    var original_language : String?,
    var genre_ids : List<Int>?,
    var popularity : Float?,
    var release_date : String?,
    var vote_average : Float,
    var vote_count : Int,
)

@Serializable
data class ContingutDTO(
    var adult: Boolean,
    var backdrop_path: String?,
    var budget : Int?,
    var genres : List<genres>?,
    var homepage : String?,
    var id : Int?,
    var imdb_id : String?,
    var origin_country : List<String>?,
    var original_language: String?,
    var original_title: String?,
    var overview: String?,
    var popularity: Float,
    var poster_path: String?,
    var status : String?,
    var tagline : String?,
    var title: String?,
    var video : Boolean?,
    var vote_average: Float?,
    var vote_count: Int?,
    var episode_run_time : List<Int>?,
    var first_air_date : String?,
    var in_production : Boolean?,
    var languages : List<String>?,
    var last_air_date : String?,
    var name : String?,
    var next_episode_to_air : NextEpisodeToAir?,
    var number_of_episodes : Int?,
    var number_of_seasons : Int?,
    var original_name: String?,
    var seasons : List<season>?,
    var type : String?,
)

@Serializable
data class NextEpisodeToAir(
    var id : Int,
    var name : String,
    var overview: String,
    var vote_average: Float,
    var vote_count: Int,
    var air_date: String?,
    var episode_number: Int,
    var episode_type: String,
    var production_code: String,
    var runtime: Int?,
    var season_number: Int,
    var show_id: Int,
    var still_path: String?
)

@Serializable
data class genres(
    var id : Int,
    var name : String,
)

@Serializable
data class season(
    var air_date : String?,
    var episode_count : Int,
    var id : Int,
    var name : String,
    var overview : String?,
    var poster_path: String?,
    var season_number : Int,
    var vote_average: Float,
    var episodes : List<episodes>?,
)

@Serializable
data class episodes(
    var air_date: String?,
    var crew : List<crew>,
    var episode_number : Int,
    var guest_stars : List<guest_stars>,
    var name : String,
    var overview: String?,
    var id : String,
    var production_code : String,
    var runtime : Int?,
    var season_number: Int,
    var still_path : String?,
    var vote_average: Float,
    var vote_count: Int,
)

@Serializable
data class crew(
//    var departament : String?,
    var job : String,
    var credit_id : String,
    var adult: Boolean,
    var gender : Int,
    var id : Int,
    var known_for_department : String,
    var name : String,
    var original_name: String,
    var popularity: Float,
    var profile_path : String?,
)

@Serializable
data class guest_stars(
    var character : String,
    var credit_id: String,
    var order : Int,
    var  adult: Boolean,
    var gender: Int,
    var id : Int,
    var known_for_department : String,
    var name : String,
    var original_name : String,
    var popularity: Float,
    var profile_path: String?
)

@Serializable
data class resultatsLlancaments(
    var dates : dates?,
    var results : List<UltimsLlencaments>,
)

@Serializable
data class dates(
    var maximum : String,
    var minimum : String,
)

@Serializable
data class UltimsLlencaments(
    var adult: Boolean,
    var backdrop_path: String?,
    var genre_ids: List<Int>?,
    var id : Int,
    var original_language: String?,
    var original_title: String,
    var overview: String?,
//    var media_type: String?,
    var popularity: Float,
    var poster_path: String?,
    var release_date: String,
    var title : String,
    var video: Boolean,
    var vote_average: Float,
    var vote_count: Int,
)

@Serializable
data class Seguiment(
    var seguimentsId : Int?,
    var segueix : Int,
    var esSeguit : Int,
    var esSeguitNavigation : Usuari?,
    var segueixNavigation : Usuari?,
)

@Serializable
data class resultatsRecomanacions(
    var results : List<recomendation>,
)

@Serializable
data class recomendation(
    var backdrop_path: String?,
    var id: Int?,
    var title: String?,
    var original_title: String?,
    var overview: String?,
    var poster_path: String?,
    var media_type: String?,
    var adult: Boolean?,
    var original_language: String?,
    var genre_ids: List<Int>?,
    var popularity: Float?,
    var release_date: String?,
    var video: Boolean,
    var vote_average: Float?,
    var vote_count: Int?,
    var name : String?,
    var original_name: String?,
    var first_air_date: String?,
    var origin_country: List<String>?,
)

@Serializable
data class proveidors(
    var id: Int,
    var results: resultsProveidors?
)

@Serializable
data class resultsProveidors(
    var es: ES
)

@Serializable
data class ES(
    var link: String,
    var flatrate: List<flatrate>
)

@Serializable
data class flatrate(
    var logo_path: String,
    var provider_id: Int,
    var provider_name: String,
    var display_priority: Int
)
