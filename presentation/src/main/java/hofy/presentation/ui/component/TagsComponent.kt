package hofy.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hofy.presentation.R
import hofy.presentation.ui.model.TagItem

@Composable
fun TagsComponent(
    tags: List<TagItem>,
    onTagClick: (TagItem.TagVO) -> Unit,
    onCancelClick: () -> Unit
) {
    LazyRow(Modifier.padding(bottom = 8.dp), contentPadding = PaddingValues(horizontal = 8.dp)) {
        items(tags) {
            TagComponent(it, onTagClick, onCancelClick)
        }
    }
}

fun Modifier.getTagStyleModifier(selected: Boolean, color: Long): Modifier {
    return if (selected) {
        background(Color(color), RoundedCornerShape(16.dp))
    } else {
        border(1.dp, Color(color), RoundedCornerShape(16.dp))
    }
}

@Composable
fun TagComponent(
    tag: TagItem,
    onTagClick: ((TagItem.TagVO) -> Unit)? = null,
    onCancelClick: (() -> Unit)? = null
) {
    when (tag) {
        TagItem.Cancel -> {
            Icon(
                painterResource(R.drawable.ic_close),
                null,
                modifier = Modifier.clickable { onCancelClick?.invoke() })
        }
        is TagItem.TagVO -> {
            val selected = tag.selected || onTagClick == null
            Text(
                tag.name,
                modifier = Modifier.padding(horizontal = 4.dp)
                    .getTagStyleModifier(selected, tag.color)
                    .clickable {
                        onTagClick?.invoke(tag)
                    }.padding(vertical = 2.dp, horizontal = 12.dp),
                fontWeight = FontWeight.Light,
                color = if (selected) Color.White else Color.Black
            )
        }
    }
}