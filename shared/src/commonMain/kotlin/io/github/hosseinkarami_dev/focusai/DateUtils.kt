package io.github.hosseinkarami_dev.focusai

private const val MILLIS_PER_DAY = 86_400_000L

/** Converts a Gregorian calendar date to noon UTC so locale formatting keeps the chosen day stable. */
fun isoDateToEpochMillis(value: String?): Long? {
    if (value == null) return null
    val match = Regex("^(\\d{4})-(\\d{2})-(\\d{2})$").matchEntire(value) ?: return null
    val year = match.groupValues[1].toIntOrNull() ?: return null
    val month = match.groupValues[2].toIntOrNull() ?: return null
    val day = match.groupValues[3].toIntOrNull() ?: return null
    if (year !in 1..9999 || month !in 1..12) return null
    val leap = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    val monthLength = when (month) {
        2 -> if (leap) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
    if (day !in 1..monthLength) return null

    val adjustedYear = year - if (month <= 2) 1 else 0
    val era = adjustedYear / 400
    val yearOfEra = adjustedYear - era * 400
    val shiftedMonth = month + if (month > 2) -3 else 9
    val dayOfYear = (153 * shiftedMonth + 2) / 5 + day - 1
    val dayOfEra = yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear
    return (era * 146_097L + dayOfEra - 719_468L) * MILLIS_PER_DAY + MILLIS_PER_DAY / 2
}

fun epochMillisToIsoDate(epochMillis: Long?): String? {
    if (epochMillis == null) return null
    val z = floorDiv(epochMillis, MILLIS_PER_DAY) + 719_468L
    val era = floorDiv(z, 146_097L)
    val dayOfEra = (z - era * 146_097L).toInt()
    val yearOfEra = (dayOfEra - dayOfEra / 1_460 + dayOfEra / 36_524 - dayOfEra / 146_096) / 365
    var year = yearOfEra + era.toInt() * 400
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val monthPrime = (5 * dayOfYear + 2) / 153
    val day = dayOfYear - (153 * monthPrime + 2) / 5 + 1
    val month = monthPrime + if (monthPrime < 10) 3 else -9
    year += if (month <= 2) 1 else 0
    return "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
}

private fun floorDiv(value: Long, divisor: Long): Long {
    val quotient = value / divisor
    return if ((value xor divisor) < 0 && quotient * divisor != value) quotient - 1 else quotient
}
