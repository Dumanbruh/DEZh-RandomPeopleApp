package com.mobilefinal.randpeoples

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.mobilefinal.randpeoples.model.User
import com.mobilefinal.randpeoples.theme.JetpackComposeSimpreRestApiTheme
import com.mobilefinal.randpeoples.theme.Grey350
import com.mobilefinal.randpeoples.theme.Grey600
import com.valentinilk.shimmer.shimmer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeSimpreRestApiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MyApp1()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        setContent {
            JetpackComposeSimpreRestApiTheme {
                Loader()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setContent {
            JetpackComposeSimpreRestApiTheme {
                MyApp1()
            }
        }
    }

    private fun toastMeState(message: String) {
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        toastMeState("Main menu")
    }
}

@Composable
fun MyApp1(
    viewModel: UserViewModel = hiltViewModel()
) {
    val users by viewModel.users.observeAsState(arrayListOf())
    val isLoading by viewModel.isLoading.observeAsState(false)

    MyApp(onAddClick = {
        viewModel.addUser()
    }, onDeleteClick = {
        viewModel.deleteUser(it)
    }, users, isLoading)

}

@Composable
fun Loader(
    viewModel: UserViewModel = hiltViewModel()
) {
    MyApp(onAddClick = {
        viewModel.addUser()
    }, onDeleteClick = {
        viewModel.deleteUser(it)
    }, emptyList(), true)

}


@Composable
fun MyApp(

    onAddClick: (() -> Unit)? = null,
    onDeleteClick: ((toDelete: User) -> Unit)? = null,
    users: List<User>,
    isLoading: Boolean,
) {
    var cardBgColor = Grey350;
    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
        cardBgColor = Grey600
    }
    val mContext = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Random Peoples") },
                actions = {
                    IconButton(onClick = {
                        onAddClick?.invoke()
                    }) {
                        Icon(Icons.Filled.Add, "Add")
                    }
                    IconButton(onClick = { mContext.startActivity(Intent(mContext, SettingsActivity::class.java))}) {
                        Icon(Icons.Rounded.Settings, "NightMode")
                    }
                }
            )
        }
    ) {
        LazyColumn { // Alternative for RecyclerView
            var itemCount = users.size
            if (isLoading) itemCount++

            items(count = itemCount) { index ->
                var auxIndex = index
                if (isLoading) {
                    if (auxIndex == 0)
                        return@items LoadingCard()
                    auxIndex--
                }
                val user = users[auxIndex]
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 1.dp,
                    backgroundColor = cardBgColor,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = rememberImagePainter(
                                data = user.thumbnail,
                                builder = {
                                    placeholder(R.drawable.placeholder)
                                    error(R.drawable.placeholder)
                                }
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight
                        )
                        Spacer()
                        Column(
                            Modifier.weight(1f),
                        ) {
                            Text("${user.name} ${user.lastName}")
                            Text(user.city)
                        }
                        Spacer()
                        IconButton(onClick = {
                            onDeleteClick?.invoke(user)
                        }) {
                            Icon(Icons.Filled.Delete, "Remove")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingCard() {
    var cardBgColor = Grey350;
    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
        cardBgColor = Grey600
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 1.dp,
        backgroundColor = cardBgColor,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .testTag("loadingCard")
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            ImageLoading()
            Spacer()
            Column {
                Spacer()
                Box(modifier = Modifier.shimmer()) {
                    Column {
                        Box(
                            modifier = Modifier
                                .height(15.dp)
                                .fillMaxWidth()
                                .background(Color.Gray)
                        )
                        Spacer()
                        Box(
                            modifier = Modifier
                                .height(15.dp)
                                .fillMaxWidth()
                                .background(Color.Gray)
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ImageLoading() {
    Box(modifier = Modifier.shimmer()) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Gray)
        )
    }
}

@Composable
fun Spacer(size: Int = 8) = Spacer(modifier = Modifier.size(size.dp))

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeSimpreRestApiTheme {
        MyApp(isLoading = true, users = arrayListOf())
    }
}