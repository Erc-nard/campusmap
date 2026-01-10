package com.example.campusmap.ui.map

import com.google.android.gms.maps.model.CameraPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun CampusMapScreen(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    markerPosition: LatLng
) {
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = markerPosition),
            title = "Marker"
        )
    }
}
