package com.example.push.core.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.push.emotion.data.repository.EmotionRepository
import com.example.push.emotion.presentation.EmotionViewModel
import com.example.push.emotion.presentation.EmotionViewModelFactory
import com.example.push.home.presentation.HomeUI
import com.example.push.login.data.repository.LoginRepository
import com.example.push.login.domain.LoginUseCase
import com.example.push.login.presentation.LoginScreen
import com.example.push.login.presentation.LoginViewModel
import com.example.push.login.presentation.LoginViewModelFactory
import com.example.push.notes.data.repository.NoteRepository
import com.example.push.notes.domain.GetNotesUseCase
import com.example.push.notes.domain.PostNotesUseCase
import com.example.push.notes.presentation.NewNoteScreen
import com.example.push.notes.presentation.NoteViewModel
import com.example.push.notes.presentation.NoteViewModelFactory
import com.example.push.notes.presentation.NotesUI
import com.example.push.register.data.repository.RegisterRepository
import com.example.push.register.domain.RegisterUseCase
import com.example.push.register.presentation.RegisterScreen
import com.example.push.register.presentation.RegisterViewModel
import com.example.push.register.presentation.RegisterViewModelFactory

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val NOTES = "notes"
    const val NEW_NOTE = "new_note"
    const val HOME = "home"
    const val EMOTION_TRACKER = "emotion"
}

@Composable
fun NavigationWrapper() {
    val loginRepository = LoginRepository()
    val registerRepository = RegisterRepository()
    val noteRepository = NoteRepository()
    val context = LocalContext.current
    val navController = rememberNavController()

    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val target = sharedPreferences.getString("navigateTo", null)
    val startDestination = when {
        sharedPreferences.getBoolean("isLoggedIn", false) -> "Home"
        else -> "Login"
    }


    if (startDestination != null) {

        // ViewModels corregidos
        val loginViewModel: LoginViewModel = viewModel(
            factory = LoginViewModelFactory(
                loginUseCase = LoginUseCase(loginRepository),
                navigateToHome = { navController.navigate(AppRoutes.HOME) },
                context = context,
                navigateToRegister = { navController.navigate(AppRoutes.REGISTER) }
            )
        )


        val registerViewModel: RegisterViewModel = viewModel(
            factory = RegisterViewModelFactory(
                registerUseCase = RegisterUseCase(registerRepository),
                navigateLogin = { navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) } }
            )
        )

        val noteViewModel: NoteViewModel = viewModel(
            factory = NoteViewModelFactory(
                getNotesUseCase = GetNotesUseCase(noteRepository),
                postNotesUseCase = PostNotesUseCase(noteRepository)
            )
        )
        NavHost(
            navController = navController, startDestination = startDestination
        ) {

            composable(AppRoutes.HOME) {
                HomeUI(
                    navigateToNotes = { navController.navigate(AppRoutes.NOTES) },
                    navigateToEmotion = { /* lo que quieras */ },
                    onLogout = {
                        // Borrar token y userId
                        val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        prefs.edit().clear().apply()

                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }


            composable(AppRoutes.NOTES) {
                NotesUI(
                    noteViewModel = noteViewModel,
                    navigateToNewNote = { navController.navigate(AppRoutes.NEW_NOTE) }
                )
            }



            composable("login") {
                LoginScreen(viewModel = loginViewModel)
            }

            composable("register") {
                RegisterScreen(viewModel = registerViewModel)
            }


            composable(AppRoutes.NEW_NOTE) {
                val sharedPrefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("access_token", "") ?: ""

                val emotionViewModel: EmotionViewModel = viewModel(
                    factory = EmotionViewModelFactory(EmotionRepository(token))
                )

                NewNoteScreen(
                    noteViewModel = noteViewModel,
                    emotionViewModel = emotionViewModel,
                    onNoteCreated = {
                        navController.popBackStack(AppRoutes.NOTES, false)
                    }
                )
            }


        }
    }
}