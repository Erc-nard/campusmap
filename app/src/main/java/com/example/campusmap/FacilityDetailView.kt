package com.example.campusmap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import coil3.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat

@Composable
fun FacilityDetailView(itemData: FacilityItem, onMoveToMap: (LatLng) -> Unit) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    Row {
        if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
            AsyncImage(
                model = itemData.imageURL,
                contentDescription = itemData.title,
                modifier = Modifier.fillMaxHeight().weight(1f),
                contentScale = ContentScale.Crop
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
                Modifier.fillMaxHeight().weight(2f)
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
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                item {
                    Text(
                        text = itemData.title,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 32.dp)
                    )
                }
            }
            item {
                DetailView("위치") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Column {
                            Text(itemData.details.location.description)
                            if (!itemData.details.location.annotation.isNullOrBlank()) {
                                Text(
                                    text = itemData.details.location.annotation,
                                    color = Color.Gray
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
                    DetailView("운영 시간") {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
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
                                    Text(mealHours.timeDuration)
                                }
                            }
                        }
                    }
                }
            }
            if (itemData.details.upcomingMenus.isNotEmpty()) {
                item {
                    DetailView("메뉴") {
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
            }
            if (itemData.details.contact.isNotEmpty()) {
                item {
                    ContactView(itemData)
                }
            }
        }
    }
}