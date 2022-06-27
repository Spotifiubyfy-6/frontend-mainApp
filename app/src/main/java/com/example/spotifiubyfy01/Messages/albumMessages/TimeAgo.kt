package com.example.spotifiubyfy01.Messages.albumMessages

import java.util.*
import java.util.concurrent.TimeUnit

val times: List<Long> = Arrays.asList(
    TimeUnit.DAYS.toMillis(365),
    TimeUnit.DAYS.toMillis(30),
    TimeUnit.DAYS.toMillis(1),
    TimeUnit.HOURS.toMillis(1),
    TimeUnit.MINUTES.toMillis(1),
    TimeUnit.SECONDS.toMillis(1)
)
val timesString: List<String> = Arrays.asList("y", "mo", "d", "h", "min", "s")

fun toDuration(duration: Long): String {
    val res = StringBuffer()
    for (i in times.indices) {
        val current: Long = times.get(i)
        val temp = duration / current
        if (temp > 0) {
            res.append(temp).append(" ").append(timesString.get(i)).append(" ago")
            break
        }
    }
    return if ("" == res.toString()) "0 seconds ago" else res.toString()
}