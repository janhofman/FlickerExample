package hofy.presentation.ui.component.base

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import hofy.presentation.R

@Composable
fun AppImage(
    model: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    Box(contentAlignment = Alignment.Center) {
        val placeholderState = remember { mutableStateOf(true) }
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(1000))
        )
        AsyncImage(
            model = model,
            contentDescription = null,
            modifier = modifier,
            onState = {
                placeholderState.value = it is AsyncImagePainter.State.Loading
            },
            contentScale = contentScale,
        )
        Image(
            painterResource(id = R.drawable.ic_flicker),
            modifier = Modifier.alpha(if (placeholderState.value) 1f else 0f).size(48.dp)
                .rotate(rotation),
            contentDescription = null,
        )
    }
}