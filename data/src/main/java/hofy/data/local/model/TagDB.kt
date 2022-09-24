package hofy.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hofy.data.repository.PURPLE
import hofy.domain.model.TagDO

@Entity
data class TagDB(
    @PrimaryKey
    val name: String,
    @ColumnInfo(name = "color") val color: Long?
) {
    fun toDO(): TagDO {
        return TagDO(name, color ?: PURPLE)
    }
}