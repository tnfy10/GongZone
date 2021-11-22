package kr.co.wanted.gongzone.model.geocode

data class AddressElement(
    val code: String,
    val longName: String,
    val shortName: String,
    val types: List<String>
)