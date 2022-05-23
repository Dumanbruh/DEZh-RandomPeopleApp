package com.mobilefinal.randpeoples

import android.content.ContentValues.TAG
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HiltTest { // Hilt UI Testing using Dagger
    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addingItemsWorksCorrectly() {
        composeTestRule.setContent {
            MyApp1()
        }

        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Name 0 LastName 0").assertExists()

        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Name 1 LastName 1").assertExists()
    }

    @Test
    fun removingItemsWorkCorrectly() {
        composeTestRule.setContent {
            MyApp1()
        }
        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Name 0 LastName 0").assertExists()

        composeTestRule.onNodeWithContentDescription("Remove").performClick()
        composeTestRule.onNodeWithText("Name 0 LastName 0").assertDoesNotExist()
    }

    @Test
    fun loadingIsVisibleWhenAddingNewItem() {
        composeTestRule.setContent {
            MyApp(users = emptyList(), isLoading = true)
        }

        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithTag("loadingCard").assertExists()
    }

    @Test
    fun setDarkTheme(){
        composeTestRule.setContent {
            MyContent()
        }

        composeTestRule.onNodeWithTag("nightMode")
            .assertIsDisplayed()
            .performClick()

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
            Log.d(TAG,"Error!")
        }
    }
}
