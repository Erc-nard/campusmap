package com.example.campusmap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.CachePolicy
import coil3.svg.SvgDecoder
import com.example.campusmap.ui.theme.appBackground
import com.example.campusmap.ui.theme.selectedBackground
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.launch
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import me.saket.telephoto.zoomable.rememberZoomableState
import kotlin.math.absoluteValue

@Composable
fun BuildingMapView(data: BuildingMap) {
    val scope = rememberCoroutineScope()

    val hazeState = remember { HazeState() }

    val building = buildings[data.code]!!
    val initialFloorValue = data.data.keys.map { it.absoluteValue }.minOf { it }
    val initialFloor = if (data.data.keys.contains(initialFloorValue)) initialFloorValue else -initialFloorValue
    var currentFloor by remember { mutableStateOf(initialFloor) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .hazeChild(
                        state = hazeState, blurRadius = 20.dp
                    )
                    .background(appBackground.copy(alpha = 0.9f))
            ) {
                Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "뒤로"
                        )
                    }
                    Text(
                        text = building.buildingDescription,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp),
                ) {
                    items(items = data.data.keys.toList().sorted()) { item ->
                        val floorLabel = if (item > 0) "${item}층" else "${-item+1}층"
                        Text(
                            text = floorLabel,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (currentFloor == item) null else FontWeight.Light,
                            fontSize = 17.sp,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(48.dp))
                                .background(
                                    if (currentFloor == item) selectedBackground.copy(
                                        0.5f
                                    ) else Color.Transparent
                                )
                                .clickable {
                                    scope.launch {
                                        currentFloor = item
                                    }
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val state = rememberTransformableState { zoomChange, offsetChange, _ ->
            scale *= zoomChange
            offset += offsetChange
        }

        val zoomableState = rememberZoomableImageState()

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .transformable(state = state)
                .haze(state = hazeState)
        ) {
            val imageURL = data.data[currentFloor]!!
            val extension = imageURL.substringAfterLast(".")

            if (extension.toLowerCase() == "svg") {
                val context = LocalContext.current
                val imageLoader = ImageLoader.Builder(context)
                    .components {
                        add(SvgDecoder.Factory()) // SVG 디코더 추가
                    }
                    .build()

                ZoomableAsyncImage(
                    model = imageURL,
                    contentDescription = building.buildingDescription + " " + currentFloor,
                    imageLoader = imageLoader,
                    state = zoomableState,
                    modifier = Modifier.fillMaxSize()
                )
//                SubcomposeAsyncImage(
//                    model = imageURL,
//                    contentDescription = building.buildingDescription + " " + currentFloor,
//                    imageLoader = imageLoader,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .graphicsLayer(
//                            scaleX = scale.coerceIn(1f, 5f), // 최소 1배, 최대 5배 제한
//                            scaleY = scale.coerceIn(1f, 5f),
//                            translationX = offset.x,
//                            translationY = offset.y
//                        )
//                ) {
//                    val state = painter.state
//                    if (state is AsyncImagePainter.State.Error) {
//                        Text("이미지 로드에 실패했습니다.")
//                    } else {
//                        SubcomposeAsyncImageContent()
//                    }
//                }
            } else {
//                SubcomposeAsyncImage(
//                    model = imageURL,
//                    contentDescription = building.buildingDescription + " " + currentFloor,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .graphicsLayer(
//                            scaleX = scale.coerceIn(1f, 5f), // 최소 1배, 최대 5배 제한
//                            scaleY = scale.coerceIn(1f, 5f),
//                            translationX = offset.x,
//                            translationY = offset.y
//                        )
//                ) {
//                    val state = painter.state
//                    if (state is AsyncImagePainter.State.Error) {
//                        Text("이미지 로드에 실패했습니다.")
//                    } else {
//                        SubcomposeAsyncImageContent()
//                    }
//                }
            }
        }
    }
}