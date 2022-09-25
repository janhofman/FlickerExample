package hofy.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import hofy.presentation.ui.component.base.AppImage
import hofy.presentation.ui.model.PhotoVO

@OptIn(ExperimentalUnitApi::class)
@Composable
fun LargePhotoItem(
    photo: PhotoVO,
    onImageClick: (PhotoVO) -> Unit,
    onShareClick: (PhotoVO) -> Unit,
    onDownloadClick: (PhotoVO) -> Unit,
    onFavouriteClick: (PhotoVO) -> Unit
) {
    Text(
        photo.title,
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
            photo.author,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            fontSize = TextUnit(10f, TextUnitType.Sp)
        )
    }
    AppImage(
        photo.url,
        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable {
            onImageClick.invoke(photo)
        }.padding(top = 8.dp),
        contentScale = ContentScale.Crop
    )
    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Text(
            photo.published,
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
                modifier = Modifier.padding(horizontal = 4.dp)
                    .clickable { onShareClick.invoke(photo) }.padding(4.dp)
            )
            Icon(
                if (photo.isFavourite) {
                    painterResource(R.drawable.ic_favourite)
                } else {
                    painterResource(R.drawable.ic_favourite_empty)
                },
                null,
                Modifier.padding(horizontal = 4.dp).clickable { onFavouriteClick.invoke(photo) }
                    .padding(4.dp)
            )
            Icon(
                painterResource(R.drawable.ic_download),
                null,
                Modifier.padding(4.dp).clickable { onDownloadClick.invoke(photo) }
                    .padding(horizontal = 4.dp)
            )
        }
        FlowRow(
            crossAxisSpacing = 4.dp,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            photo.tags.forEach { TagComponent(it) }
        }
    }
    Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
}