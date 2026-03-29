package com.angelina.lvivexplorer.navigation

sealed class Destinations(val route: String) {
    data object Map : Destinations("map?focusPlaceId={focusPlaceId}") {
        const val baseRoute: String = "map"
        fun routeWithFocus(placeId: String): String = "map?focusPlaceId=$placeId"
    }
    data object Filter : Destinations("filter")
    data object Diary : Destinations("diary")
    data object Settings : Destinations("settings")
    data object Detail : Destinations("detail/{placeId}") {
        fun routeWithArg(placeId: String): String = "detail/$placeId"
    }
}
