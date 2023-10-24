package com.dpfht.android.casestudy123.framework.data.datasource.local.room.converter

import androidx.room.TypeConverter
import java.util.Date

class TypeTransmogrifier {

  @TypeConverter
  fun fromDate(date: Date?): Long? {
    return date?.time
  }

  @TypeConverter
  fun toDate(millisSinceEpoch: Long?): Date? {
    return millisSinceEpoch?.let { Date(it) }
  }
}
