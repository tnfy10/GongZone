package kr.co.wanted.gongzone.model.geocode

data class Geocode(
    val addresses: List<Addresse>,
    val errorMessage: String,
    val meta: Meta,
    val status: String
)