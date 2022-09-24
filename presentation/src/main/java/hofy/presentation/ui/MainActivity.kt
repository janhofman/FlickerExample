package hofy.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hofy.presentation.R
import hofy.presentation.ui.component.BottomInfoComponent
import hofy.presentation.ui.component.FlickerAppBar
import hofy.presentation.ui.component.screen.PhotoDetailScreen
import hofy.presentation.ui.main.MainScreen
import hofy.presentation.ui.model.PhotoVO
import hofy.presentation.ui.theme.FlickerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()
    private var photoToDownload: PhotoVO? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.state.value
            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            FlickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            Column(Modifier.fillMaxWidth().height(56.dp).background(Color.White)) {
                                Spacer(
                                    Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray)
                                )
                                Row(
                                    Modifier.fillMaxWidth().height(55.dp).background(Color.White),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Icon(
                                        modifier = Modifier.clickable {
                                            navController.navigate("main")
                                        }.padding(8.dp),
                                        painter = painterResource(R.drawable.ic_continuous),
                                        contentDescription = null
                                    )
                                    Icon(
                                        modifier = Modifier.clickable {
                                            navController.navigate("favourites")
                                        }.padding(8.dp),
                                        painter = painterResource(R.drawable.ic_favourite),
                                        contentDescription = null
                                    )
                                }
                            }


                        },
                        topBar = {
                            FlickerAppBar(
                                state,
                                currentBackStackEntry,
                                onNavigationIconClick = { navController.navigateUp() },
                                onPhotoDownloadClick = { handleImageDownload(it) },
                                onModeChangeClick = { viewModel.changeMode() },
                                onShareIconClick = { shareImage(it) },
                                onSearchChanged = { viewModel.loadImages(it) })
                        }) {
                        Box(
                            Modifier.fillMaxSize().padding(bottom = 56.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(Modifier.fillMaxSize()) {
                                NavHost(navController = navController, startDestination = "main") {
                                    composable("main") {
                                        MainScreen(state, {
                                            viewModel.triggerPhotoDetail(it)
                                            navController.navigate(
                                                "detail/${
                                                    URLEncoder.encode(
                                                        it.url,
                                                        StandardCharsets.UTF_8.toString()
                                                    )
                                                }"
                                            )
                                        }, {
                                            shareImage(it)
                                        }, {
                                            handleImageDownload(it)
                                        }, {
                                            viewModel.loadImages()
                                        }, {
                                            viewModel.triggerTagClicked(it)
                                        }, {
                                            viewModel.triggerFavouriteClicked(it)
                                        })
                                    }
                                    composable("detail/{photoUrl}") { navBackStackEntry ->
                                        PhotoDetailScreen(
                                            URLDecoder.decode(
                                                navBackStackEntry.arguments?.getString(
                                                    "photoUrl"
                                                ).orEmpty(), StandardCharsets.UTF_8.toString()
                                            )
                                        )
                                    }
                                    composable("favourites") {
                                        Text("favourites")
                                    }
                                }
                            }
                            BottomInfoComponent(state)
                        }
                    }
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                photoToDownload?.let {
                    viewModel.triggerDownloadImage(it)
                }
                photoToDownload = null
            }
        }

    private fun handleImageDownload(photoVO: PhotoVO) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                viewModel.triggerDownloadImage(photoVO)
            }
            else -> {
                photoToDownload = photoVO
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun shareImage(photoVO: PhotoVO) {
        ShareCompat.IntentBuilder(this)
            .setType("text/plain")
            .setChooserTitle(R.string.share_image)
            .setText(photoVO.link ?: photoVO.url)
            .startChooser()
    }
}


