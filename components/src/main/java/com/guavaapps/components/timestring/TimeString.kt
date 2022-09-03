package com.guavaapps.components.timestring

import java.util.concurrent.TimeUnit

open class TimeString(private val milliseconds: Long) {
    private val components: MutableList<String> = mutableListOf()

    override fun toString(): String {
        return components.joinToString("")
    }

    fun separator(s: String) {
        components.add(s);
    }

    fun milliseconds() {
        milliseconds("%d");
    }

    fun milliseconds(f: String) {
        val c: Long = milliseconds
        -TimeUnit.SECONDS.toMillis(milliseconds);

        components.add(String.format(f, c));
    }

    fun seconds() {
        seconds("%d");
    }

    fun seconds(f: String) {
        val c: Long = (
                TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
                );

        components.add(String.format(f, c));
    }

    fun minutes() {
        minutes("%d");
    }

    fun minutes(f: String) {
        val c: Long = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        -TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));

        components.add(String.format(f, c));
    }
}