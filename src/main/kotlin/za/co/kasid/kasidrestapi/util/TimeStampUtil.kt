package za.co.kasid.kasidrestapi.util

import za.co.kasid.kasidrestapi.model.WhatsAppSession
import kotlin.math.abs

object TimeStampUtil {

    fun isTimeStampExpired(session: WhatsAppSession): Boolean {
        val t1: Long = session.sessionUnixTimeStamp.toLong()
        val t2: Long = System.currentTimeMillis() / 1000L
        val minutes = abs(t1 - t2) / 60
        println(minutes)
        return minutes > 10
    }

}