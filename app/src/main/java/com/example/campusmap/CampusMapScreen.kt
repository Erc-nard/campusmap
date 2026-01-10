package com.example.campusmap.ui.map

import android.os.Build
import com.google.android.gms.maps.model.CameraPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

// 건물 도형 데이터
data class BuildingPolygon(
    val id: String, // 건물 번호를 저장
    val points: List<LatLng>
)

@Composable
fun CampusMapScreen(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    mapProperties: MapProperties,
    markerPosition: LatLng
) {
    val buildings = remember {
        listOf(
            BuildingPolygon(
                id = "E3",
                points = listOf(
                    LatLng(36.36873178141499, 127.36458014303662), // 파스쿠찌 쪽 점
                    LatLng(36.3686510424991, 127.36445997947378),
                    LatLng(36.36859703238999, 127.36444022739323),
                    LatLng(36.3681362505657, 127.36482807960252),
                    LatLng(36.3679718994553, 127.36479109798243),
                    LatLng(36.36791538982127, 127.3648521199115),
                    LatLng(36.36790381052078, 127.3649551372814),
                    LatLng(36.36793967820769, 127.36501380482284),
                    LatLng(36.368106282147224, 127.36505079741939),
                    LatLng(36.36808783328771, 127.36518999728246),
                    LatLng(36.36800001277097, 127.36517565828161),
                    LatLng(36.367990686091034, 127.36527868624363),
                    LatLng(36.367947940612396, 127.36525898642142),
                    LatLng(36.36788585117788, 127.36567098183544),
                    LatLng(36.36794662831159, 127.3656879805336),
                    LatLng(36.36794198608103, 127.36573253026066),
                    LatLng(36.36772133834167, 127.36568692625882),
                    LatLng(36.367693706540614, 127.36588179643157),
                    LatLng(36.36793235180408, 127.36593584238993),
                    LatLng(36.36794149990958, 127.3658913137784),
                    LatLng(36.367986506713244, 127.36590823881588),
                    LatLng(36.367945212028545, 127.36615040272729),
                    LatLng(36.36813433605872, 127.36619028900203),
                    LatLng(36.36817583558858, 127.36588126810976),
                    LatLng(36.36840773066515, 127.36593249707009),
                    LatLng(36.36841490736472, 127.3657960297831),
                    LatLng(36.3686227113159, 127.3656215009264),
                    LatLng(36.36865627460756, 127.36569687297816),
                    LatLng(36.36864724595632, 127.36570240220671),
                    LatLng(36.36883540605288, 127.36605707273004),
                    LatLng(36.368871563392915, 127.36602102737614),
                    LatLng(36.36889839333936, 127.36608801104413),
                    LatLng(36.36902710731212, 127.36599111266148),
                    LatLng(36.368791849258294, 127.36556657739354),
                    LatLng(36.36909904452107, 127.36530615338602),
                    LatLng(36.36906544692237, 127.36524192393519),
                    LatLng(36.36908352972936, 127.3652225081618),
                    LatLng(36.36882814593424, 127.36474773748084),
                    LatLng(36.36880333001979, 127.36475876463167),
                    LatLng(36.368769740625815, 127.36469174995219),
                    LatLng(36.36874717761583, 127.36470278762748),
                    LatLng(36.368702400207425, 127.36461064912721)
                )
            ),
            BuildingPolygon(
                id = "E4",
                points = listOf(
                    LatLng(36.36769790140649, 127.36377025010822),
                    LatLng(36.36806976035682, 127.3644684112558),
                    LatLng(36.368638917889555, 127.36400306145242),
                    LatLng(36.368264786429336, 127.36331045828928)
                ),
            )
        )
    }

    var selectedBuildingId by remember { mutableStateOf<String?>(null) }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        onMapClick = {
            selectedBuildingId = null
        }
    ) {
        Marker(
            state = MarkerState(position = markerPosition),
            title = "Marker"
        )
        buildings.forEach { building ->
            val isSelected = selectedBuildingId == building.id
            val buildingColor = when (building.id[0]) {
                'E' -> Color(239, 153, 37)
                'W' -> Color(111, 182, 70)
                'N' -> Color(223, 52, 48)
                else -> Color.Black
            }
            Polygon(
                points = building.points,
                fillColor = if (isSelected) buildingColor.copy(0.25f) else buildingColor.copy(0.1f),
                strokeColor = if (isSelected) buildingColor else Color.Transparent,
                strokeWidth = 3f,
                clickable = true,
                onClick = {
                    selectedBuildingId = building.id
                }
            )
        }
    }
}
