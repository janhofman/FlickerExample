package hofy.presentation.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import hofy.presentation.R
import hofy.presentation.ui.component.TagComponent
import hofy.presentation.ui.component.base.AppImage
import hofy.presentation.ui.model.PhotoVO

@OptIn(ExperimentalUnitApi::class)
@Composable
fun PhotoGrid(
    items: List<PhotoVO>,
    onImageClick: (PhotoVO) -> Unit,
    grid: Boolean,
    onShareClick: (PhotoVO) -> Unit,
    onDownloadClick: (PhotoVO) -> Unit,
    onFavouriteClick: (PhotoVO) -> Unit
) {
    if (grid) {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(items) {
                AppImage(
                    it.url, modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable {
                        onImageClick.invoke(it)
                    },
                    contentScale = ContentScale.Crop
                )
            }
        }
    } else {
        LazyColumn(Modifier.fillMaxSize()) {
            items.forEach {
                item {
                    Text(
                        it.title,
                        fontWeight = FontWeight.Medium,
                        fontSize = TextUnit(12f, TextUnitType.Sp),
                        lineHeight = TextUnit(12f, TextUnitType.Sp),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    )
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            stringResource(R.string.by),
                            modifier = Modifier.padding(end = 4.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            fontSize = TextUnit(10f, TextUnitType.Sp)
                        )
                        Text(
                            it.author,
                            fontWeight = FontWeight.Light,
                            color = Color.Gray,
                            fontSize = TextUnit(10f, TextUnitType.Sp)
                        )
                    }
                    AppImage(
                        it.url,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable {
                            onImageClick.invoke(it)
                        }.padding(top = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                        Text(
                            it.published,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Light,
                            color = Color.Gray,
                            fontStyle = FontStyle.Italic,
                            fontSize = TextUnit(10f, TextUnitType.Sp)
                        )
                        Row(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                            Icon(
                                painterResource(R.drawable.ic_share),
                                null,
                                modifier = Modifier.padding(4.dp)
                                    .clickable { onShareClick.invoke(it) }
                            )
                            Icon(
                                if (it.isFavourite) {
                                    painterResource(R.drawable.ic_favourite)
                                } else {
                                    painterResource(R.drawable.ic_favourite_empty)
                                },
                                null,
                                Modifier.clickable { onFavouriteClick.invoke(it) }.padding(4.dp)
                            )
                            Icon(
                                painterResource(R.drawable.ic_download),
                                null,
                                Modifier.padding(4.dp).clickable { onDownloadClick.invoke(it) }
                            )
                        }
                        FlowRow(
                            crossAxisSpacing = 4.dp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            it.tags.forEach { TagComponent(it) }
                        }
                    }
                    Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
                }
            }
        }
    }

}