package com.kebunby.app.util

import java.text.SimpleDateFormat
import java.util.*

class Formatter {
    companion object {
        fun formatDate(strDate: String): String {
            val localeID = Locale(Locale.getDefault().displayLanguage, "ID")
            val date = SimpleDateFormat("yyyy-MM-dd", localeID).parse(strDate)

            return SimpleDateFormat("dd MMM yyyy", localeID).format(date!!)
        }
    }
}