    package com.example.smedia.ui.theme.screens

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.smedia.MainViewModel
import com.example.smedia.R
import com.example.smedia.prefstore.PreferencesStoreImpl
import com.example.smedia.ui.theme.SMediaTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.setAccountReference(this)
        setContent {
            SMediaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(mainViewModel, this)
                }
            }
        }
    }
}

@Composable
fun MainView(mainViewModel: MainViewModel, context: Context) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = PreferencesStoreImpl(context)

    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val title = if (mainViewModel.userIsAuthenticated) {
            stringResource(id = R.string.logged_in_title)
        } else {
            if (mainViewModel.appJustLaunched) {
                stringResource(id = R.string.initial_title)
            } else {
                stringResource(id = R.string.logged_out_title)
            }
        }

        Title(
            text = title
        )

        if (mainViewModel.userIsAuthenticated) {
            UserInfoRow(
                label = stringResource(id = R.string.name_label),
                value = mainViewModel.user.name
            )
            UserInfoRow(
                label = stringResource(id = R.string.email_label),
                value = mainViewModel.user.email
            )
            UserPicture(
                url = mainViewModel.user.picture,
                description = "Description goes here"
            )
        }

        val buttonText: String
        val onClickAction: () -> Unit
        if (mainViewModel.userIsAuthenticated) {
            buttonText = stringResource(id = R.string.log_out_button)
            onClickAction = {
                mainViewModel.logout(context)
            }

        } else {
            buttonText = stringResource(id = R.string.log_in_button)
            onClickAction = {
                mainViewModel.login(context)
            }
        }
        LogButton(
            text = buttonText,
            onClick = onClickAction
        )
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    )
}

@Composable
fun UserPicture(url: String, description: String) {
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = description,
            modifier = Modifier.fillMaxSize(0.5f)
        )
    }
}

@Composable
fun UserInfoRow(label: String, value: String) {
    Row {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = value,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 20.sp
            )
        )
    }

}

@Composable
fun LogButton(text: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
        ) {
            Text(
                text = text,
                fontSize = 20.sp
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SMediaTheme {
        //MainView()
    }
}