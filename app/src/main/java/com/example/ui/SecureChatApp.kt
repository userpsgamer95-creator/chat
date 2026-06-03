package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.MainScreen
import com.example.ui.screens.OnboardingScreen
import com.example.viewmodel.ChatViewModel

const val ROUTE_MAIN = "main"
const val ROUTE_CHAT = "chat/{chatId}"

@Composable
fun SecureChatApp() {
    val navController = rememberNavController()
    val viewModel: ChatViewModel = viewModel()
    val registeredName by viewModel.registeredName.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        if (registeredName.isNullOrEmpty()) {
            OnboardingScreen(viewModel = viewModel)
        } else {
            NavHost(navController = navController, startDestination = ROUTE_MAIN) {
                composable(ROUTE_MAIN) {
                    MainScreen(navController = navController, viewModel = viewModel)
                }
                composable(ROUTE_CHAT) { backStackEntry ->
                    val chatId = backStackEntry.arguments?.getString("chatId")
                    ChatDetailScreen(navController = navController, viewModel = viewModel, chatId = chatId)
                }
            }
        }
    }
}
