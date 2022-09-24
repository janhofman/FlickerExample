package hofy.presentation.ui.ext

import androidx.compose.runtime.MutableState


fun <T> MutableState<T>.set(reduce: T.() -> T): Boolean {
    return synchronized(this) {
        unsafeSet(reduce)
    }
}

private fun <T> MutableState<T>.unsafeSet(reduce: T.() -> T): Boolean {
    val currentValue = value
    val newValue = currentValue.reduce()

    // A default mutation policy of mutableStateOf() has changed to be structuralEqualityPolicy() instead of
    // referentialEqualityPolicy(). Therefore the Structural Equality is used here and not the Referential one
    return if (currentValue != newValue) {
        value = newValue
        true
    } else {
        false
    }
}