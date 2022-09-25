package hofy.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hofy.presentation.R
import hofy.presentation.ui.main.NavigationItem

@Composable
fun BottomNavigationBar(isMainActive: Boolean, onNavigationItemClick: (NavigationItem) -> Unit) {
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
                modifier = Modifier.scale(if (isMainActive) 1.2f else 1f).clickable {
                    onNavigationItemClick.invoke(NavigationItem.Main)
                }.padding(8.dp),
                painter = painterResource(R.drawable.ic_continuous),
                contentDescription = null
            )
            Icon(
                modifier = Modifier.scale(if (isMainActive) 1f else 1.2f).clickable {
                    onNavigationItemClick.invoke(NavigationItem.Favourites)
                }.padding(8.dp),
                painter = painterResource(R.drawable.ic_favourite),
                contentDescription = null
            )
        }
    }
}