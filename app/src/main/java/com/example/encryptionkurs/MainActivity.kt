package com.example.encryptionkurs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.encryptionkurs.ui.theme.EncryptionKursTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.encryptionkurs.ui.EncryptionViewModel
import com.example.encryptionkurs.ui.theme.jetFontFamily
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptionKursTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompressItScreen()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompressItScreen(
) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var chosenButton by remember {
        mutableStateOf(0)
    }

    var isDevVisible by remember {
        mutableStateOf(false)
    }

    val viewModel: EncryptionViewModel = hiltViewModel()

    val outputText by viewModel.outputDataState.collectAsState()
    val currentUrl by viewModel.currentUrlState.collectAsState()
    var currentUrlText by remember { mutableStateOf(TextFieldValue(currentUrl)) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Compress It!", fontFamily = jetFontFamily,
                        fontStyle = FontStyle.Italic
                    )
                },
                colors = centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFd9d9d9)
                ),
                modifier = Modifier.clickable {
                    isDevVisible = !isDevVisible
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Algorithm selection buttons
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        chosenButton = 1
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (chosenButton == 1) Color(0xFFAEAEAE) else Color(
                            0xFFd9d9d9
                        ),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "REL",
                        fontFamily = jetFontFamily,
                        fontWeight = FontWeight(500)
                    )
                }

                Spacer(modifier = Modifier.width(19.dp))


                Button(
                    onClick = {
                        chosenButton = 2
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (chosenButton == 2) Color(0xFFAEAEAE) else Color(
                            0xFFd9d9d9
                        ),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "BWT",
                        fontFamily = jetFontFamily,
                        fontWeight = FontWeight(500)
                    )
                }

                Spacer(modifier = Modifier.width(19.dp))

                Button(
                    onClick = {
                        chosenButton = 3
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (chosenButton == 3)
                            Color(0xFFAEAEAE)
                        else Color(
                            0xFFd9d9d9
                        ),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "LZW",
                        fontFamily = jetFontFamily,
                        fontWeight = FontWeight(500)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                "Input",
                fontFamily = jetFontFamily,
                fontWeight = FontWeight(600),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(Alignment.Start),
            )

            // Input field
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(100.dp, Dp.Infinity),
                textStyle = TextStyle(
                    fontFamily = jetFontFamily,
                    fontStyle = FontStyle.Italic
                ),
                placeholder = {
                    Text(
                        text = "lorem ipsum",
                        fontFamily = jetFontFamily,
                        fontWeight = FontWeight(400),
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.align(Alignment.Start),
                        color = Color(0xff989898),
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    textColor = Color(0xff989898),
                    containerColor = Color(0xFFd9d9d9)
                )
            )

            Spacer(modifier = Modifier.height(17.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Button(
                    onClick = {
                        viewModel.proceedData(
                            dataToEncrypt = inputText.text,
                            algorithm = chosenButton,
                            operation = "encode"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFd9d9d9),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "Encode",
                        fontFamily = jetFontFamily,
                        fontWeight = FontWeight(700)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_separation_line),
                    modifier = Modifier.fillMaxHeight(),
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.width(14.dp))

                Button(
                    onClick = {
                        viewModel.proceedData(
                            dataToEncrypt = inputText.text,
                            algorithm = chosenButton,
                            operation = "decode"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFd9d9d9),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "Decode",
                        fontFamily = jetFontFamily,
                        fontWeight = FontWeight(700)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Output",
                fontFamily = jetFontFamily,
                fontWeight = FontWeight(600),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(Alignment.Start),
            )

            // Output field
            OutlinedTextField(
                value = outputText,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(100.dp, Dp.Infinity),
                onValueChange = { },
                textStyle = TextStyle(
                    fontFamily = jetFontFamily,
                    fontStyle = FontStyle.Italic
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    textColor = Color(0xff989898),
                    containerColor = Color(0xFFd9d9d9)
                )
            )

            if (isDevVisible) {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField (
                    value = currentUrlText,
                    onValueChange = {
                        currentUrlText = it
                    }
                )

                Button(onClick = {
                    isDevVisible = false
                    viewModel.saveUrl(currentUrlText.text) }
                ) {
                    Text("save")
                }
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CompressItScreen()
}
