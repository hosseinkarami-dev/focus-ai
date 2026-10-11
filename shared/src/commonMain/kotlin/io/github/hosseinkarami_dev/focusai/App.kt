package io.github.hosseinkarami_dev.focusai

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.key
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import io.github.hosseinkarami_dev.focusai.data.TaskEntity
import io.github.hosseinkarami_dev.focusai.data.NoteEntity
import io.github.hosseinkarami_dev.focusai.data.TaskProposal
import io.github.hosseinkarami_dev.focusai.presentation.SettingsViewModel
import io.github.hosseinkarami_dev.focusai.presentation.TodayViewModel
import io.github.hosseinkarami_dev.focusai.presentation.FocusViewModel
import io.github.hosseinkarami_dev.focusai.ui.FocusAiMark
import io.github.hosseinkarami_dev.focusai.ui.FocusText
import io.github.hosseinkarami_dev.focusai.ui.LocalAppLanguage
import io.github.hosseinkarami_dev.focusai.ui.localizedText
import io.github.hosseinkarami_dev.focusai.ui.theme.FocusTheme
import io.github.hosseinkarami_dev.focusai.publishWidgetTaskSnapshot
import io.github.hosseinkarami_dev.focusai.ui.screens.CaptureDialog
import io.github.hosseinkarami_dev.focusai.ui.screens.FocusScreen
import io.github.hosseinkarami_dev.focusai.ui.screens.InboxScreen
import io.github.hosseinkarami_dev.focusai.ui.screens.LanguagePickerDialog
import io.github.hosseinkarami_dev.focusai.ui.screens.ReviewDialog
import io.github.hosseinkarami_dev.focusai.ui.screens.SettingsScreen
import io.github.hosseinkarami_dev.focusai.ui.screens.TaskEditorScreen
import io.github.hosseinkarami_dev.focusai.ui.screens.TaskListScreen
import io.github.hosseinkarami_dev.focusai.ui.screens.TodayScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private enum class Destination(val label: String) { Today("Today"), Inbox("Inbox"), Tasks("Tasks"), Focus("Focus"), Settings("Settings") }
private data class ReviewDraft(val noteId: String, val proposals: List<TaskProposal>)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun App(initialAction: String? = null, onInitialActionHandled: () -> Unit = {}) {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val focusViewModel = koinViewModel<FocusViewModel>()
    val preferences by settingsViewModel.settings.collectAsState()
    FocusTheme(themeMode = preferences.themeMode, language = preferences.language) {
      CompositionLocalProvider(
        LocalAppLanguage provides preferences.language,
        LocalLayoutDirection provides if (preferences.language == "fa" || preferences.language == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
      ) {
        val viewModel = koinViewModel<TodayViewModel>()
        val tasks by viewModel.taskList.collectAsState()
        LaunchedEffect(tasks) {
            publishWidgetTaskSnapshot(tasks.filterNot { it.completed }.take(3).map { it.title })
        }
        val notes by viewModel.noteList.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        var destination by rememberSaveable { mutableStateOf(Destination.Today.name) }
        var showTaskEditor by rememberSaveable { mutableStateOf(false) }
        var showLanguageDialog by rememberSaveable { mutableStateOf(false) }
        var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) }
        var taskDueDatePrefill by rememberSaveable { mutableStateOf<String?>(null) }
        var showCapture by rememberSaveable { mutableStateOf(false) }
        var noteToEdit by remember { mutableStateOf<NoteEntity?>(null) }
        var review by remember { mutableStateOf<ReviewDraft?>(null) }
        val screenStateHolder = rememberSaveableStateHolder()
        LaunchedEffect(initialAction) {
            when (initialAction) {
                "io.github.hosseinkarami_dev.focusai.ADD_TASK" -> {
                    destination = Destination.Tasks.name
                    taskToEdit = null
                    taskDueDatePrefill = null
                    showTaskEditor = true
                }
                "io.github.hosseinkarami_dev.focusai.CAPTURE_NOTE" -> {
                    destination = Destination.Inbox.name
                    noteToEdit = null
                    showCapture = true
                }
                "io.github.hosseinkarami_dev.focusai.OPEN_TASKS" -> destination = Destination.Tasks.name
            }
            if (initialAction != null) onInitialActionHandled()
        }
        var fabVisible by remember { mutableStateOf(true) }
        val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        val appBarScrolled = topAppBarScrollBehavior.state.contentOffset < 0f
        val appBarElevation by animateDpAsState(
            targetValue = if (appBarScrolled) 5.dp else 0.dp,
            label = "app bar elevation"
        )
        val fabScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                    if (consumed.y < -1.5f) fabVisible = false
                    else if (consumed.y > 1.5f) fabVisible = true
                    return Offset.Zero
                }
            }
        }
        LaunchedEffect(destination, showTaskEditor) {
            fabVisible = true
            if (!showTaskEditor) {
                topAppBarScrollBehavior.state.contentOffset = 0f
                topAppBarScrollBehavior.state.heightOffset = 0f
            }
        }
        PlatformBackHandler(enabled = showTaskEditor || destination != Destination.Today.name) {
            if (showTaskEditor) {
                showTaskEditor = false
                taskToEdit = null
                taskDueDatePrefill = null
            } else {
                destination = Destination.Today.name
            }
        }

        val onToggleTask: (TaskEntity) -> Unit = { task ->
            val nextCompleted = !task.completed
            viewModel.setCompleted(task, nextCompleted)
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = if (nextCompleted) "Task completed" else "Task marked open",
                    actionLabel = "Undo",
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    viewModel.setCompleted(task, task.completed)
                }
            }
        }

        Scaffold(
            modifier = Modifier
                .nestedScroll(fabScrollConnection)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = if (!showTaskEditor) ({
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    Destination.entries.forEach { item ->
                        NavigationBarItem(
                            selected = destination == item.name,
                            onClick = { destination = item.name },
                            icon = { Icon(item.icon(), contentDescription = null) },
                            label = { FocusText(item.label) }
                        )
                    }
                }
            }) else ({}),
            floatingActionButton = {
                val canAdd = !showTaskEditor && destination in setOf(Destination.Today.name, Destination.Tasks.name, Destination.Inbox.name)
                AnimatedVisibility(
                    visible = canAdd && fabVisible,
                    enter = fadeIn() + scaleIn(initialScale = .82f) + slideInVertically { it / 2 },
                    exit = fadeOut() + scaleOut(targetScale = .82f) + slideOutVertically { it / 2 }
                ) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            if (destination == Destination.Inbox.name) {
                                noteToEdit = null
                                showCapture = true
                            } else {
                                taskToEdit = null
                                taskDueDatePrefill = null
                                showTaskEditor = true
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                        text = { FocusText(if (destination == Destination.Inbox.name) "Capture a thought" else "Add task", style = MaterialTheme.typography.labelLarge) }
                    )
                }
            }
        ) { padding ->
          Box(Modifier.fillMaxSize()) {
            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                targetState = if (showTaskEditor) "TaskEditor" else destination,
                transitionSpec = {
                    val enteringFromRight = targetState == "TaskEditor"
                    (slideInHorizontally { width -> if (enteringFromRight) width / 8 else -width / 8 } + fadeIn()) togetherWith
                        (slideOutHorizontally { width -> if (enteringFromRight) -width / 8 else width / 8 } + fadeOut())
                },
                label = "FocusAI navigation"
            ) { currentScreen ->
              screenStateHolder.SaveableStateProvider(currentScreen) {
                if (currentScreen == "TaskEditor") {
                    key(taskToEdit?.id ?: "new-task") { TaskEditorScreen(
                        task = taskToEdit, initialDueDateIso = taskDueDatePrefill, padding = padding,
                        onDismiss = { showTaskEditor = false; taskToEdit = null; taskDueDatePrefill = null },
                        onSave = { title, details, priority, estimate, dueDate, recurrence, reminderTime, subtasks ->
                            val saved = viewModel.saveTask(title, details, priority, estimate, dueDate, recurrence, reminderTime, subtasks, taskToEdit)
                            if (saved) {
                                val successMessage = localizedText(
                                    if (taskToEdit == null) "Task added" else "Task updated",
                                    preferences.language
                                )
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = successMessage,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                showTaskEditor = false
                                taskToEdit = null
                                taskDueDatePrefill = null
                            }
                            saved
                        },
                        onOpenNotificationSettings = viewModel::openNotificationSettings,
                        onDelete = { task -> viewModel.deleteTask(task); showTaskEditor = false; taskToEdit = null; taskDueDatePrefill = null }
                    ) }
                } else when (Destination.valueOf(currentScreen)) {
                Destination.Today -> TodayScreen(tasks, padding, onCapture = { noteToEdit = null; showCapture = true }, onFocus = { destination = Destination.Focus.name }, onToggle = onToggleTask, onEditTask = { taskDueDatePrefill = null; taskToEdit = it; showTaskEditor = true })
                Destination.Tasks -> TaskListScreen(
                    tasks,
                    padding,
                    onToggle = onToggleTask,
                    onEditTask = { taskDueDatePrefill = null; taskToEdit = it; showTaskEditor = true },
                    onMoveTask = { task, direction -> viewModel.moveTask(task, direction) },
                    onAddTask = { dueDate -> taskToEdit = null; taskDueDatePrefill = dueDate; showTaskEditor = true }
                )
                Destination.Inbox -> InboxScreen(
                    notes = notes,
                    padding = padding,
                    onOpenNote = { note -> noteToEdit = note; showCapture = true },
                    onDeleteNote = { note ->
                        scope.launch {
                            viewModel.deleteNote(note.id)
                            val result = snackbarHostState.showSnackbar(
                                message = localizedText("Note deleted", preferences.language),
                                actionLabel = localizedText("Undo", preferences.language),
                                withDismissAction = true,
                                duration = SnackbarDuration.Long
                            )
                            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                                viewModel.restoreNote(note)
                            }
                        }
                    },
                    onArchiveNote = { note, archived -> viewModel.setNoteArchived(note.id, archived) }
                )
                Destination.Focus -> {
                    val activeSession by focusViewModel.active.collectAsState()
                    val history by focusViewModel.history.collectAsState()
                    val allSessions by focusViewModel.allSessions.collectAsState()
                    val insightTasks by focusViewModel.tasks.collectAsState()
                    FocusScreen(
                        tasks = tasks, insightTasks = insightTasks, allSessions = allSessions,
                        padding = padding, focusLengthMinutes = preferences.focusLengthMinutes,
                        dailyFocusGoalMinutes = preferences.dailyFocusGoalMinutes,
                        activeSession = activeSession, history = history,
                        onStart = focusViewModel::start, onPause = focusViewModel::pause,
                        onResume = focusViewModel::resume,
                        onFinish = {
                            focusViewModel.finish(
                                preferences.breakRemindersEnabled,
                                localizedText("FocusAI break", preferences.language),
                                localizedText("Great work. Take a short break.", preferences.language)
                            )
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    localizedText(
                                        if (preferences.breakRemindersEnabled) "Great work. Take a short break." else "Focus session complete",
                                        preferences.language
                                    ),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        onDiscard = focusViewModel::discard
                    )
                }
                Destination.Settings -> SettingsScreen(
                    padding = padding,
                    preferences = preferences,
                    onThemeMode = settingsViewModel::setThemeMode,
                    onFocusLength = settingsViewModel::setFocusLength,
                    onDailyFocusGoal = settingsViewModel::setDailyFocusGoal,
                    onBreakRemindersEnabled = settingsViewModel::setBreakRemindersEnabled,
                    onOpenNotificationSettings = viewModel::openNotificationSettings,
                    onLanguage = { showLanguageDialog = true }
                )
                }
              }
            }
            AnimatedVisibility(
                visible = showTaskEditor || appBarScrolled,
                modifier = Modifier.align(Alignment.TopCenter).zIndex(1f),
                enter = fadeIn() + slideInVertically { -it / 3 },
                exit = fadeOut() + slideOutVertically { -it / 3 }
            ) {
                TopAppBar(
                    modifier = Modifier.shadow(appBarElevation),
                    title = {
                        if (showTaskEditor) {
                            FocusText(if (taskToEdit == null) "New task" else "Task details", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        } else {
                            AnimatedVisibility(
                                visible = appBarScrolled,
                                enter = fadeIn() + slideInVertically { -it / 3 },
                                exit = fadeOut() + slideOutVertically { -it / 3 }
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    FocusAiMark(34.dp)
                                    FocusText("FocusAI", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        if (showTaskEditor) IconButton(onClick = { showTaskEditor = false; taskToEdit = null }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = localizedText("Back", preferences.language))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    scrollBehavior = topAppBarScrollBehavior
                )
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = if (showTaskEditor || appBarScrolled) 76.dp else 12.dp
                    )
                    .zIndex(2f)
            ) { data ->
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(18.dp),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        FocusText(data.visuals.message, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        data.visuals.actionLabel?.let { label ->
                            TextButton(onClick = data::performAction) {
                                FocusText(label, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        if (data.visuals.withDismissAction) {
                            IconButton(onClick = data::dismiss) {
                                Icon(Icons.Filled.Close, contentDescription = localizedText("Dismiss", preferences.language))
                            }
                        }
                    }
                }
            }
          }
        }

        if (showLanguageDialog) LanguagePickerDialog(
            selectedLanguage = preferences.language,
            onSelect = { settingsViewModel.setLanguage(it); showLanguageDialog = false },
            onDismiss = { showLanguageDialog = false }
        )
        if (showCapture) CaptureDialog(
            existingNote = noteToEdit,
            onDismiss = { showCapture = false },
            onSaved = { showCapture = false; noteToEdit = null },
            onReview = { noteId, proposals -> showCapture = false; noteToEdit = null; review = ReviewDraft(noteId, proposals) }
        )
        review?.let { draft ->
            ReviewDialog(draft.proposals, onDismiss = { review = null }, onConfirm = { selected ->
                viewModel.confirm(selected, draft.noteId)
                review = null
            })
        }
      }
    }
}

@Composable
private fun Destination.icon() = when (this) {
    Destination.Today -> Icons.Filled.Home
    Destination.Inbox -> Icons.Filled.Inbox
    Destination.Tasks -> Icons.Filled.TaskAlt
    Destination.Focus -> Icons.Filled.Timer
    Destination.Settings -> Icons.Filled.Settings
}
