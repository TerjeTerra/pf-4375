package no.uio.ifi.in2000.election.oblig2.model.votes

enum class District {
    D1,
    D2,
    D3
}

fun getDistrict(): List<District> {
    val districtList = mutableListOf<District>()
    districtList.add(District.D1)
    districtList.add(District.D2)
    districtList.add(District.D3)

    return districtList
}