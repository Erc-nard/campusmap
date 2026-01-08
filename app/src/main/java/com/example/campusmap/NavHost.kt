package com.example.campusmap

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Serializable object Facilities
@Serializable data class FacilityCategory(val id: Int, val title: String, val items: Array<FacilityItem>)
@Serializable data class FacilityItem(val id: Int, val title: String, val imageURL: String)

@Composable
fun FacilitiesNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Facilities) {
        composable<Facilities> {
            Facilities()
        }
//        composable<FacilityCategory> { backstackEntry ->
//            val category: FacilityCategory = backstackEntry.toRoute()
//            FacilitiesCategoryView(data = category.dataReference)
//        }
//        composable<FacilityItem> { backstackEntry ->
//            val item: FacilityItem = backstackEntry.toRoute()
//            Text(item.title)
//        }
    }
}
