package com.example.campusmap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import coil3.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat

@Composable
fun FacilityDetailView(itemData: FacilityItem, onMoveToMap: (LatLng) -> Unit, getCurrentLocation: ((LatLng?) -> Unit) -> Unit = {}) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    var distanceString by remember {
        mutableStateOf<String?>(null)
    }

    getCurrentLocation { location ->
        distanceString = null
        location?.let { currentLocation ->
            val startLocation = android.location.Location("start").apply {
                latitude = currentLocation.latitude
                longitude = currentLocation.longitude
            }
            val endLocation = android.location.Location("end").apply {
                latitude = itemData.details.coordinate.latitude
                longitude = itemData.details.coordinate.longitude
            }
            val calculatedDistance = startLocation.distanceTo(endLocation)
            distanceString = if (calculatedDistance >= 1000f)
                "%.2f km".format(calculatedDistance)
            else
                "%.0f m".format(calculatedDistance)
        }
    }

    Row {
        if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
            AsyncImage(
                model = itemData.imageURL,
                contentDescription = itemData.title,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 40.dp),
            modifier = if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
                Modifier
                    .fillMaxHeight()
                    .weight(2f)
            } else {
                Modifier.fillMaxSize()
            }
        ) {
            if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT) {
                item {
                    AsyncImage(
                        model = itemData.imageURL,
                        contentDescription = itemData.title,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    Text(
                        text = itemData.title,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            } else {
                item {
                    Text(
                        text = itemData.title,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 40.dp)
                    )
                }
            }
            item {
                DetailView("위치") { innerPadding ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = innerPadding)
                    ) {
                        Column {
                            Text(itemData.details.location.description)
                            if (!itemData.details.location.annotation.isNullOrBlank()) {
                                Text(
                                    text = (if (distanceString != null) "${distanceString!!} · " else "") + itemData.details.location.annotation,
                                    fontWeight = FontWeight.Light
                                )
                            } else if (distanceString != null) {
                                Text(
                                    text = distanceString!!,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        }
                        if (itemData.details.coordinate != LatLng(0.0, 0.0)) {
                            Spacer(modifier = Modifier.weight(1f))
                            FilledIconButton(onClick = {
                                onMoveToMap(itemData.details.coordinate)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "지도에서 보기"
                                )
                            }
                        }
                    }
                }
            }
            if (itemData.details.businessHours.isNotEmpty()) {
                item {
                    BusinessHoursView(itemData.details.businessHours)
                }
            } else if (itemData.details.mealHours.isNotEmpty()) {
                item {
                    DetailView("운영 시간") { innerPadding ->
                        Column(
                            modifier = Modifier.padding(horizontal = innerPadding)
                        ) {
                            itemData.details.mealHours.forEach { mealHours ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = mealHours.name,
                                        modifier = Modifier.width(80.dp)
                                    )
                                    Text(
                                        text = mealHours.timeDuration,
                                        fontWeight = FontWeight.Light
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (itemData.details.upcomingMenus.isNotEmpty()) {
                item {
                    DetailView("메뉴") { innerPadding ->
                        val convertedData =
                            itemData.details.upcomingMenus.map { item ->
                                var bodyText = item.menu.joinToString("\n")
                                if (item.price > 0) {
                                    val priceLine = "₩${DecimalFormat("#,###").format(item.price)}\n"
                                    bodyText = priceLine + bodyText
                                }
                                TitledText(item.title, bodyText)
                            }
                        Carousel(convertedData)
                    }
                }
            } else if (itemData.details.topMenus.isNotEmpty()) {
                item {
                    DetailView("대표 메뉴") { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(horizontal = innerPadding)
                        ) {
//                            itemData.details.topMenus.forEach { menu ->
//                                Text(menu)
//                            }
                            Text(itemData.details.topMenus.joinToString(", "))
                        }
                    }
                }
            }
            if (itemData.details.contact.isNotEmpty()) {
                item {
                    ContactView(itemData)
                }
            }
        }
    }
}