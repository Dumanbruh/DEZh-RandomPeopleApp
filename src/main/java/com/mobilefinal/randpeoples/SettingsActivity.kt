package com.mobilefinal.randpeoples

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobilefinal.randpeoples.theme.JetpackComposeSimpreRestApiTheme
import com.mobilefinal.randpeoples.theme.Teal200

class SettingsActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetpackComposeSimpreRestApiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    settingsContent()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"Theme was successfully changed",Toast.LENGTH_LONG).show();
    }
}

// Creating a composable
// function to display Top Bar
@Composable
fun settingsContent() {
    val activity = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(onClick = {
                        activity.startActivity(Intent(activity, MainActivity::class.java))
                    }){
                        Icon(Icons.Rounded.ArrowBack, "NightMode")
                    }
                },
                title = { Text("Settings", color = Color.White) },
                backgroundColor = Teal200
            ) },
        content = { MyContent() }
    )
}

// Creating a composable function to
// create two Images and a spacer between them
// Calling this function as content in the above function
@Composable
fun MyContent(){
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
        val checkedState = remember { mutableStateOf(false) }
        val activity = LocalContext.current as Activity

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            checkedState.value = true;
        }
        else if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
            checkedState.value = false;
        }

        Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)){
            Text("Dark mode", fontSize = 22.sp, modifier = Modifier.padding(horizontal = 5.dp))

            Switch(
                modifier = Modifier.testTag("nightMode"),
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    changeTheme(checkedState.value)
                    activity.recreate()
                }
            )
            changeTheme(checkedState.value)
        }
    }
}

fun changeTheme(isChecked: Boolean){
    if (isChecked) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}

// For displaying preview in
// the Android Studio IDE emulator
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    settingsContent()
}