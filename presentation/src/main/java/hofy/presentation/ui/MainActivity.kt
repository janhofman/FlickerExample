package hofy.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hofy.presentation.R
import hofy.presentation.ui.component.BottomNavigationBar
import hofy.presentation.ui.component.FlickerAppBar
import hofy.presentation.ui.main.MainScreen
import hofy.presentation.ui.main.NavigationItem
import hofy.presentation.ui.model.PhotoVO
import hofy.presentation.ui.navigation.isDetailNavigation
import hofy.presentation.ui.navigation.isMainNavigation
import hofy.presentation.ui.theme.FlickerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

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
                            if (!currentBackStackEntry.isDetailNavigation()) {
                                BottomNavigationBar(currentBackStackEntry.isMainNavigation()) {
                                    navController.navigate(it.path)
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
                        MainScreen(
                            navController,
                            state,
                            {
                                viewModel.triggerPhotoDetail(it)
                                navController.navigate(
                                    NavigationItem.Detail.build(it)
                                )
                            },
                            { shareImage(it) },
                            { handleImageDownload(it) },
                            { viewModel.loadImages() },
                            { viewModel.triggerTagClicked(it) },
                            { viewModel.triggerFavouriteClicked(it) },
                            { viewModel.triggerCancelTags() })
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


