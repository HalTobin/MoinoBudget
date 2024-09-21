package util

fun Int.nullIfMinus(): Int? = if (this == -1) null else this