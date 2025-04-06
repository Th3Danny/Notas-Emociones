package com.example.push.core.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.push.emotion.data.repository.EmotionRepository
import com.example.push.emotion.domain.PostEmotionRecordUseCase
import com.example.push.emotion.domain.PostEmotionUseCase
import com.example.push.emotion.presentation.*
import com.example.push.home.presentation.HomeUI
import com.example.push.login.data.repository.LoginRepository
import com.example.push.login.domain.LoginUseCase
import com.example.push.login.presentation.*
import com.example.push.notes.data.repository.NoteRepository
import com.example.push.notes.domain.GetNotesUseCase
import com.example.push.notes.domain.PostNotesUseCase
import com.example.push.notes.presentation.*
import com.example.push.register.data.repository.RegisterRepository
import com.example.push.register.domain.RegisterUseCase
import com.example.push.register.presentation.*

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val NOTES = "notes"
    const val NEW_NOTE = "new_note"
    const val HOME = "home"
    const val EMOTION_TRACKER = "emotion"
    const val NEW_EMOTION = "new_emotion"
    const val EMOTION_RECORD = "emotion_record/{emotionId}"

    fun emotionRecord(emotionId: Int) = "emotion_record/$emotionId"
}

@Composable
fun NavigationWrapper() {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val loginRepository = LoginRepository()
    val registerRepository = RegisterRepository()
    val noteRepository = NoteRepository()
    val navController = rememberNavController()

    val startDestination = if (sharedPrefs.getBoolean("isLoggedIn", false)) AppRoutes.HOME else AppRoutes.LOGIN

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
            navigateLogin = {
                navController.navigate(AppRoutes.LOGIN) {
                    popUpTo(AppRoutes.LOGIN)
                }
            }
        )
    )

    val noteViewModel: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(
            getNotesUseCase = GetNotesUseCase(noteRepository),
            postNotesUseCase = PostNotesUseCase(noteRepository)
        )
    )

    NavHost(navController = navController, startDestination = startDestination) {

        composable(AppRoutes.HOME) {
            HomeUI(
                navigateToNotes = { navController.navigate(AppRoutes.NOTES) },
                navigateToEmotion = { navController.navigate(AppRoutes.EMOTION_TRACKER) },
                navigateToNewEmotion = { navController.navigate(AppRoutes.NEW_EMOTION) },
                navigateToRecordEmotion = { emotionId ->
                    navController.navigate(AppRoutes.emotionRecord(emotionId))
                },
                onLogout = {
                    sharedPrefs.edit().clear().apply()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.NOTES) {
            NotesUI(
                noteViewModel = noteViewModel,
                navigateToNewNote = { navController.navigate(AppRoutes.NEW_NOTE) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(viewModel = loginViewModel)
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(viewModel = registerViewModel)
        }

        composable(AppRoutes.NEW_NOTE) {
            val repo = EmotionRepository()
            val emotionViewModel: EmotionViewModel = viewModel(
                factory = EmotionViewModelFactory(
                    repo,
                    PostEmotionUseCase(repo),
                    PostEmotionRecordUseCase(repo)
                )
            )
            NewNoteScreen(
                noteViewModel = noteViewModel,
                emotionViewModel = emotionViewModel,
                onNoteCreated = { navController.popBackStack(AppRoutes.NOTES, false) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.NEW_EMOTION) {
            val repo = EmotionRepository()
            val emotionViewModel: EmotionViewModel = viewModel(
                factory = EmotionViewModelFactory(
                    repo,
                    PostEmotionUseCase(repo),
                    PostEmotionRecordUseCase(repo)
                )
            )
            NewEmotionScreen(
                emotionViewModel = emotionViewModel,
                onEmotionCreated = { navController.popBackStack(AppRoutes.EMOTION_TRACKER, false) }
            )
        }

        composable(
            route = AppRoutes.EMOTION_RECORD,
            arguments = listOf(navArgument("emotionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val emotionId = backStackEntry.arguments?.getInt("emotionId") ?: return@composable

            val repo = EmotionRepository()
            val emotionViewModel: EmotionViewModel = viewModel(
                factory = EmotionViewModelFactory(
                    repo,
                    PostEmotionUseCase(repo),
                    PostEmotionRecordUseCase(repo)
                )
            )

            EmotionRecordScreen(
                viewModel = emotionViewModel,
                emotionId = emotionId,
                onRecordSaved = {
                    navController.popBackStack(AppRoutes.HOME, false)
                }
            )
        }
    }
}
