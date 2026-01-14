package com.example.campusmap.ui.map

import android.os.Build
import com.google.android.gms.maps.model.CameraPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import com.example.campusmap.buildings
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlin.collections.forEach

@Composable
fun CampusMapScreen(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    mapProperties: MapProperties,
    mapUiSettings: MapUiSettings,
    markerPositionsState: MutableState<List<LatLng>>,
    selectedBuildingState: MutableState<String?>,
    onFloorTap: () -> Unit,
    onBuildingTap: (String) -> Unit,
) {
//    var selectedBuildingId by remember { mutableStateOf<String?>(null) }

    GoogleMap(
        modifier = modifier,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        onMapClick = {
            selectedBuildingState.value = null
            onFloorTap()
        }
    ) {
        markerPositionsState.value.forEach { position ->
            Marker(
                state = MarkerState(position = position)
            )
        }
        buildings.forEach { (code, building) ->
            val isSelected = selectedBuildingState.value == code
            val buildingColor = when (building.code[0]) {
                'E' -> Color(239, 153, 37)
                'W' -> Color(111, 182, 70)
                'N' -> Color(223, 52, 48)
                else -> Color.Black
            }
            if (building.polygon.isNotEmpty()) {
                Polygon(
                    points = building.polygon,
                    fillColor = if (isSelected) buildingColor.copy(0.4f) else buildingColor.copy(
                        0.2f
                    ),
                    strokeColor = if (isSelected) buildingColor else Color.Transparent,
                    strokeWidth = 3f,
                    clickable = true,
                    onClick = {
                        onBuildingTap(code)
                    }
                )
            }
        }
    }
}
