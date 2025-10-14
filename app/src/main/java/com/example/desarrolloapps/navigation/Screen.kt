package com.example.desarrolloapps.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home_page")
    data object Profile : Screen("Profile_page")
    data object Settings : Screen("settings_page")


    data class Detail(val itemId: String) : Screen("detail_page/{itemId}"){
        fun buildRoute(): String{
            return route.replace("{itemId}", itemId)
        }

    }

}