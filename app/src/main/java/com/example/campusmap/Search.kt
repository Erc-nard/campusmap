package com.example.campusmap

import android.location.Location
import com.google.android.gms.maps.model.LatLng

sealed interface SearchQuery {
    data class Text(val text: String)
    data class Category(val category: String)
    data class BuildingCode(val buildingCode: String)
}

class SearchResult(
    val placeReference: PlaceData? = null,
    val buildingReference: BuildingData? = null
) {
    val isInvalid: Boolean
        get() = (placeReference == null && buildingReference == null)
    val name: String
        get() = placeReference?.getTitle() ?: buildingReference!!.name
    val category: String
        get() = placeReference?.category ?: buildingReference!!.categories.joinToString(", ")
    val locationDescription: String
        get() = if (placeReference == null) buildingReference!!.code else buildings[placeReference.buildingCode]?.description ?: ""
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
}

fun getSearchResult(query: SearchQuery): List<SearchResult> {
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
                if (building.categories.any { it.displayText == query.category })
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

    return results.toList()
}