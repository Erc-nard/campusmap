package com.example.campusmap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage

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
fun DetailView(data: ItemDetail, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = data.title,
            style = MaterialTheme.typography.titleLarge
        )
        data.content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilitiesNavigation(padding: PaddingValues) {
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
    val showLocateButton: Boolean = navBackStackEntry?.destination?.route?.substringAfterLast(".") == "FacilityItemRoute/{categoryIndex}/{index}"

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
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showLocateButton,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = {},
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text("지도에서 보기")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FacilitiesGraph,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) // 오른쪽에서 시작
            },
            exitTransition = {
                // 기존 화면은 가만히 있거나 살짝 왼쪽으로 밀림 (덮는 느낌을 주려면 0이나 작은 값)
                slideOutHorizontally(targetOffsetX = { -it / 3 })
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it / 3 })
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) // 오른쪽으로 나감
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation<FacilitiesGraph>(startDestination = Facilities) {
                composable<Facilities> {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background // 시스템 배경색 적용
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
                        color = MaterialTheme.colorScheme.background // 시스템 배경색 적용
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
                        color = MaterialTheme.colorScheme.background // 시스템 배경색 적용
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
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
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                            items(items = itemData.details) { detail ->
                                DetailView(detail, modifier = Modifier.padding(horizontal = 10.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
