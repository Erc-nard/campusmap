package com.example.campusmap.ui.map

import com.google.android.gms.maps.model.CameraPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun CampusMapScreen(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    mapProperties: MapProperties,
    markerPosition: LatLng
) {
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties
    ) {
        Marker(
            state = MarkerState(position = markerPosition),
            title = "Marker"
        )
    }
}
