package hofy.data.local.util

import java.text.SimpleDateFormat
import java.util.*


val DATE_FORMAT = SimpleDateFormat("HH:mm dd.MM.YYYY")

fun Date?.readable(): String {
    return if (this == null) {
        ""
    } else {
        DATE_FORMAT.format(this)
    }

}