package com.example.campusmap

import android.location.Location
import com.google.android.gms.maps.model.LatLng

sealed interface SearchQuery {
    data class Text(val text: String): SearchQuery
    data class Category(val category: String): SearchQuery
    data class BuildingCode(val buildingCode: String): SearchQuery
}

class SearchResult(
    val placeReference: PlaceData? = null,
    val buildingReference: BuildingData? = null
) {
    val isInvalid: Boolean
        get() = (placeReference == null && buildingReference == null)
    val name: String
        get() = placeReference?.title ?: buildingReference!!.name
    val category: String
        get() = placeReference?.category ?: buildingReference!!.category?.displayText ?: ""
    val locationDescription: String
        get() = if (placeReference == null)
            buildingReference!!.code
        else if (buildings[placeReference.buildingCode] != null) {
            buildings[placeReference.buildingCode]!!.buildingDescription + (if (placeReference.floor != null) " ${placeReference.floor}층" else "")
        } else "건물 정보 없음"
    val coordinates: LatLng?
        get() = if (placeReference == null) buildingReference!!.coordinates else placeReference.getPlaceCoordinates()
    fun getDistance(currentLocation: LatLng): Float? {
        if (coordinates == null)
            return null
        else {
            val startPosition = Location("start").apply {
                latitude = currentLocation.latitude
                longitude = currentLocation.longitude
            }
            val endPosition = Location("end").apply {
                latitude = coordinates!!.latitude
                longitude = coordinates!!.longitude
            }
            return startPosition.distanceTo(endPosition)
        }
    }
    val description: String
        get() = placeReference?.description ?: buildingReference!!.description
    val facilityCategoryReference: Int?
        get() = placeReference?.facilityCategoryReference ?: buildingReference?.facilityCategoryReference
    val facilityItemReference: Int?
        get() = placeReference?.facilityItemReference ?: buildingReference?.facilityItemReference
}

fun getSearchResult(query: SearchQuery, currentLocation: LatLng? = null): List<SearchResult> {
    val results = mutableListOf<SearchResult>()

    when (query) {
        is SearchQuery.Text -> {
            buildings.forEach { (_, building) ->
                if (building.testTextQuery(query.text))
                    results.add(SearchResult(buildingReference = building))
            }
            places.forEach { place ->
                if (place.testTextQuery(query.text))
                    results.add(SearchResult(placeReference = place))
            }
        }
        is SearchQuery.Category -> {
            buildings.forEach { (_, building) ->
                if (building.category?.displayText == query.category)
                    results.add(SearchResult(buildingReference = building))
            }
            places.forEach { place ->
                if (place.category == query.category)
                    results.add(SearchResult(placeReference = place))
            }
        }
        is SearchQuery.BuildingCode -> {
            buildings[query.buildingCode]?.let { building ->
                results.add(SearchResult(buildingReference = building))
            }
            places.forEach { place ->
                if (place.buildingCode == query.buildingCode)
                    results.add(SearchResult(placeReference = place))
            }
        }
    }

    if (currentLocation != null) {
        results.sortWith(Comparator { lhs, rhs ->
            val lhsDistance = lhs.getDistance(currentLocation) ?: Float.MAX_VALUE
            val rhsDistance = rhs.getDistance(currentLocation) ?: Float.MIN_VALUE
            (lhsDistance - rhsDistance).toInt()
        })
    }

    return results.toList()
}