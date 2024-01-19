/*
 * Copyright (c) 2023, Glassdoor Inc.
 *
 * Licensed under the Glassdoor Inc Hiring Assessment License.
 * You may not use this file except in compliance with the License.
 * You must obtain explicit permission from Glassdoor Inc before sharing or distributing this file.
 * Mention Glassdoor Inc as the source if you use this code in any way.
 */

package com.glassdoor.intern.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.glassdoor.intern.presentation.IMainViewModel
import com.glassdoor.intern.presentation.MainIntent.HideErrorMessage
import com.glassdoor.intern.presentation.MainIntent.RefreshScreen
import com.glassdoor.intern.presentation.MainUiState
import com.glassdoor.intern.presentation.model.HeaderUiModel
import com.glassdoor.intern.presentation.model.ItemUiModel
import com.glassdoor.intern.presentation.theme.InternTheme
import com.glassdoor.intern.presentation.ui.component.ContentComponent
import com.glassdoor.intern.presentation.ui.component.ErrorMessageComponent
import com.glassdoor.intern.presentation.ui.component.TopBarComponent

@Composable
internal fun MainScreen(
    viewModel: IMainViewModel,
    modifier: Modifier = Modifier,
) {
    /**
     * TODO: [Consume UI state safely from the ViewModel](https://developer.android.com/codelabs/jetpack-compose-advanced-state-side-effects#3)
     */
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBarComponent(
                modifier = Modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
                progressClickAction = { viewModel.acceptIntent(RefreshScreen) },
            )
        },
        snackbarHost = {
            ErrorMessageComponent(
                modifier = Modifier.fillMaxWidth(),
                errorMessage = uiState.errorMessage,
                hideErrorMessageAction = { viewModel.acceptIntent(HideErrorMessage) },
            )
        },
        content = { paddingValues ->
            ContentComponent(
                modifier = Modifier.padding(paddingValues),
                header = uiState.header,
                items = uiState.items,
            )
        },
    )
}

@Preview
@Composable
private fun MainScreenPreview() = InternTheme {
    val uiState = MainUiState(
        errorMessage = "An error occurred",
        header = HeaderUiModel("Sample Header"),
        isLoading = false,
        items = List(3) { index ->
            ItemUiModel(
                title = "Item $index",
                description = "Description for Item $index",
                imageUrl = null,
                timestamp = "2022-01-18"
            )
        }
    )

    MainScreen(viewModel = uiState.asDummyViewModel)
}
