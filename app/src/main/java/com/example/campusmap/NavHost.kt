package com.example.campusmap

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat

interface FacilityData {
    val id: Int
    val title: String
    val imageURL: String
}

@Serializable object FacilitiesGraph
@Serializable object Facilities
@Serializable
data class FacilityCategoryRoute(val index: Int)
@Serializable
data class FacilityItemRoute(val categoryIndex: Int, val index: Int)

@Composable
fun DetailView(title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        content()
    }
}

@Composable
fun Carousel(contents: List<TitledText>) {
    val pagerState = rememberPagerState(pageCount = { contents.size })

    Column() {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            contentPadding = PaddingValues(horizontal = 30.dp),
            pageSpacing = 10.dp
        ) { page ->
            val content = contents[page]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column() {
                        Text(
                            text = content.title,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(content.body)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilitiesNavigation(padding: PaddingValues, onMoveToMap: (LatLng) -> Unit) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val title = when (navBackStackEntry?.destination?.route?.substringAfterLast(".")) {
        "Facilities" -> "시설"
        "FacilityCategoryRoute/{index}" -> {
            val categoryItem: FacilityCategoryRoute? = navBackStackEntry?.toRoute()
            val index = categoryItem?.index
            if (index != null) {
                topLevelFacilitiesList[index].title
            } else { "시설" }
        }
        "FacilityItemRoute/{categoryIndex}/{index}" -> {
            val item: FacilityItemRoute? = navBackStackEntry?.toRoute()
            val categoryIndex = item?.categoryIndex
            val index = item?.index
            if (categoryIndex != null && index != null) {
                topLevelFacilitiesList[categoryIndex].items[index].title
            } else { "시설" }
        }
        else -> "내 앱"
    }
    val showNavigationIcon: Boolean = navBackStackEntry?.destination?.route?.substringAfterLast(".") != "Facilities"

    @Composable
    fun ImageTextView(id: Int, imageURL: String, title: String, onItemClick: (Int) -> Unit) {
        val outerPadding = 8.dp
        val innerPadding = 8.dp

        Column(
            modifier = Modifier
                .clickable { onItemClick(id) }
                .padding(outerPadding)
        ) {
            AsyncImage(
                model = imageURL,
                contentDescription = title,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(innerPadding)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = title,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    @Composable
    fun <ItemType : FacilityData> ColumnView(data: List<ItemType>, onItemClick: (Int) -> Unit) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ) {
            items(data) { item ->
                ImageTextView(
                    id = item.id,
                    imageURL = item.imageURL,
                    title = item.title,
                    onItemClick = onItemClick
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (showNavigationIcon) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FacilitiesGraph,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it / 3 })
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it / 3 })
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it })
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation<FacilitiesGraph>(startDestination = Facilities) {
                composable<Facilities> {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ColumnView(data = topLevelFacilitiesList, onItemClick = { itemId ->
                            navController.navigate(FacilityCategoryRoute(index = itemId))
                        })
                    }
                }
                composable<FacilityCategoryRoute> { backStackEntry ->
                    val route: FacilityCategoryRoute = backStackEntry.toRoute()
                    val categoryData = topLevelFacilitiesList[route.index]
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ColumnView(data = categoryData.items, onItemClick = { itemId ->
                            navController.navigate(
                                FacilityItemRoute(
                                    categoryIndex = route.index,
                                    index = itemId
                                )
                            )
                        })
                    }
                }
                composable<FacilityItemRoute> { backStackEntry ->
                    val route: FacilityItemRoute = backStackEntry.toRoute()
                    val itemData = topLevelFacilitiesList[route.categoryIndex].items[route.index]
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                AsyncImage(
                                    model = itemData.imageURL,
                                    contentDescription = itemData.title,
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                            item {
                                Text(
                                    text = itemData.title,
                                    style = MaterialTheme.typography.headlineLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
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
                                    val allDays = setOf(DayClass.WEEKDAYS, DayClass.SATURDAY, DayClass.SUNDAY)
                                    val determinedDays = itemData.details.businessHours.map { businessHours -> businessHours.days }.flatten().toSet()
                                    val undeterminedDays = allDays.subtract(determinedDays)
                                    val holidays = itemData.details.businessHours.map { businessHours -> businessHours.includeHolidays }
                                    val isHolidaysDetermined = holidays.contains(true) || holidays.contains(null)
                                    DetailView("영업 시간") {
                                        Column(
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        ) {
                                            itemData.details.businessHours.forEach { businessHours ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = businessHours.dayDescription,
                                                        modifier = Modifier.width(120.dp)
                                                    )
                                                    Text(businessHours.timeDuration)
                                                }
                                            }
                                            if (undeterminedDays.isNotEmpty() || !isHolidaysDetermined) {
                                                Row {
                                                    val breakDayDescription =
                                                        if (isHolidaysDetermined) {
                                                            if (undeterminedDays == setOf(DayClass.SATURDAY)) {
                                                                "토요일"
                                                            } else if (undeterminedDays == setOf(DayClass.SUNDAY)) {
                                                                "일요일"
                                                            } else {
                                                                "주말"
                                                            }
                                                        } else {
                                                            if (undeterminedDays == setOf(DayClass.SATURDAY, DayClass.SUNDAY)) {
                                                                "주말·공휴일"
                                                            } else if (undeterminedDays == setOf(DayClass.SATURDAY)) {
                                                                "토요일·공휴일"
                                                            } else if (undeterminedDays == setOf(DayClass.SUNDAY)) {
                                                                "일요일·공휴일"
                                                            } else {
                                                                "공휴일"
                                                            }
                                                        }
                                                    Text(
                                                        text = breakDayDescription,
                                                        modifier = Modifier.width(120.dp)
                                                    )
                                                    Text("휴무")
                                                }
                                            }
                                        }
                                    }
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
                                        val convertedData = itemData.details.upcomingMenus.map { item ->
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
                                    DetailView("연락처") {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(itemData.details.contact)
                                            Spacer(modifier = Modifier.weight(1f))
                                            val context = LocalContext.current
                                            FilledIconButton(
                                                onClick = {
                                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                                        data = Uri.parse("tel:${itemData.details.contact}")
                                                    }
                                                    context.startActivity(intent)
                                                },
//                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Call,
                                                    contentDescription = "전화 걸기"
                                                )
                                            }
                                            val clipboardManager = LocalClipboardManager.current
                                            FilledIconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(itemData.details.contact))
                                                },
//                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "복사"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
