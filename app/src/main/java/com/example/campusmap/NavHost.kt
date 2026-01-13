package com.example.campusmap

import androidx.compose.foundation.layout.*
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.campusmap.ui.theme.appBackground
import com.example.campusmap.ui.theme.selectedBackground
import com.google.android.gms.maps.model.LatLng
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.launch

interface FacilityData {
    val id: Int
    val title: String
    val imageURL: String
}

@Serializable object FacilitiesGraph
@Serializable object Facilities
@Serializable
data class FacilityItemRoute(val categoryIndex: Int, val index: Int)

@Composable
fun DetailView(title: String, modifier: Modifier = Modifier, content: @Composable (Dp) -> Unit) {
    val padding = 20.dp
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = padding)
            .shadow( //그림자옵션
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color(0xFF5FBEEB),
                spotColor = Color(0x5525739B),
                clip = false
            ),
        shape = RoundedCornerShape(padding),
        color = Color.White,
    ) {
        Column(
            modifier = modifier
                .padding(vertical = padding)
        ) {
            Text(
                text = title, //시설이름
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = padding).padding(bottom = 12.dp)
            )
            content(padding)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilitiesNavigation(padding: PaddingValues, navController: NavHostController, onMoveToMap: (LatLng) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { topLevelFacilitiesList.size })
    val scope = rememberCoroutineScope()

    val hazeState = remember { HazeState() }

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
        }
    ) {
        navigation<FacilitiesGraph>(startDestination = Facilities) {
            // 카테고리별 보기
            composable<Facilities> {
                Scaffold(
                    topBar = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .hazeChild(state = hazeState, blurRadius = 20.dp)
                                .background(appBackground.copy(alpha = 0.5f))
                        ) {
                            // 상태 막대만큼 비우기
                            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

                            // 최상단 막대
                            Row {
                                Text(
                                    text = "시설",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier
                                        .padding(16.dp, 12.dp)
                                )
                            }

                            // 2단계 탭 바
                            LazyRow(
                                contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp),
                            ) {
                                items(topLevelFacilitiesList.size) { index ->
                                    val item = topLevelFacilitiesList[index]
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (pagerState.currentPage == index) null else FontWeight.Light,
                                        fontSize = 17.sp,
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(48.dp))
                                            .background(if (pagerState.currentPage == index) selectedBackground.copy(0.5f) else Color.Transparent)
                                            .clickable {
                                                scope.launch {
                                                    pagerState.animateScrollToPage(index)
                                                }
                                            }
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    HorizontalPager(
                        state = pagerState,
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        ColumnView(
                            data = topLevelFacilitiesList[page].items,
                            onItemClick = { itemId ->
                                navController.navigate(
                                    FacilityItemRoute(
                                        categoryIndex = page,
                                        index = itemId
                                    )
                                )
                            },
                            innerPadding = innerPadding,
                            modifier = Modifier
                                .haze(state = hazeState),
                        )
                    }
                }
            }

            // 시설 카테고리 화면
            composable<FacilityItemRoute> { backStackEntry ->
                val route: FacilityItemRoute = backStackEntry.toRoute()
                val itemData = topLevelFacilitiesList[route.categoryIndex].items[route.index]
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        FacilityDetailView(itemData, onMoveToMap)

                        // 뒤로 가기 버튼
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            },
                            modifier = Modifier
                                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "뒤로"
                            )
                        }
                    }
                }
            }
        }
    }
}
