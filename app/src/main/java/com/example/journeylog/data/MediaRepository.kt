package com.example.journeylog.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.flow.flow
import java.io.File

class MediaRepository(private val context: Context) {
    data class MediaEntry(
        val uri: Uri,
        val filename: String,
        val mimeType: String,
        val size: Long,
        val path: String
    ) {
        val file: File
            get() = File(path)
    }

    fun fetchImages() = flow {
        val externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATA,
        )

        val cursor = context.contentResolver.query(
            externalContentUri,
            projection,
            null,
            null,
            "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        ) ?: throw Exception("Query could not be executed")

        cursor.use {
            while (cursor.moveToNext()) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)

                val contentUri = ContentUris.withAppendedId(
                    externalContentUri,
                    cursor.getLong(idColumn)
                )

                emit(
                    MediaEntry(
                        uri = contentUri,
                        filename = cursor.getString(displayNameColumn),
                        size = cursor.getLong(sizeColumn),
                        mimeType = cursor.getString(mimeTypeColumn),
                        path = cursor.getString(dataColumn),
                    )
                )
            }
        }
    }
}