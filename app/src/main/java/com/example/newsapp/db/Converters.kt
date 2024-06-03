package com.example.newsapp.db

import androidx.room.TypeConverter
import com.example.newsapp.model.SourceModel

class Converters {
    // Room chi chứa những kiểu dữ liệu nguyên thủy
    // nên cần sử dụng Type converter để handle qua lại giữa các kiểu dự liệu

    @TypeConverter
    fun fromSource(source: SourceModel): String {
        return source.name ?: ""
    }

    @TypeConverter
    fun toSource(name: String): SourceModel {
        return SourceModel(name, name)
    }
}