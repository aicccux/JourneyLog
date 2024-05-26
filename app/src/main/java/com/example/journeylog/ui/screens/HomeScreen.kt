package com.example.journeylog.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.journeylog.ui.Screens
import com.example.journeylog.ui.components.EmptyLogMessage
import com.example.journeylog.ui.components.LogCard
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    LaunchedEffect(Unit) { viewModel.loadLogs() }

    val state = viewModel.uiState
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(
            title = { Text("Journey Logs", fontFamily = FontFamily.Serif) },
            scrollBehavior = scrollBehavior
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate(Screens.AddLog.route) }) {
            Icon(Icons.Filled.Add, "Add log")
        }
    }) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            if (!state.loading && state.logs.isEmpty()) {
                item {
                    EmptyLogMessage(Modifier.fillParentMaxSize())
                }
            }
            items(state.logs, key = { it.date }) { log ->
                LogCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement(),
                    log = log,
                    formattedDate = viewModel.formatDateTime(log.timeInMillis),
                    onDelete = viewModel::delete
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}