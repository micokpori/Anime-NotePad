package com.rfcreations.animenotepad.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


class ShareNote(
    private val noteTitle: String,
    private val noteContent: String,
    val context: Context
) {
    fun asText() {
        val noteToShare = StringBuilder().append("$noteTitle\n\n$noteContent").toString()
        val sendIntent = Intent(Intent.ACTION_SEND).also {
            it.putExtra(Intent.EXTRA_TEXT, noteToShare)
            it.type = "text/plain"
        }
        val sendNote = Intent.createChooser(sendIntent, null)
        context.startActivity(sendNote)
    }

    fun asTextFile() {
        val noteToShare = createFile(noteTitle, noteContent, context)
        if (noteToShare != null) shareFile(noteToShare, context)
    }

}

private fun createFile(noteTitle: String, noteContent: String, context: Context): File? {
    val filename = "$noteTitle.txt"
    val outputStream: FileOutputStream?
    var file: File? = null
    try {
        // Get the files dir
        val filesDir = context.filesDir
        val fileFolder = File(filesDir, "Shared Notes")

        // Delete all previously created note files
        fileFolder.deleteRecursively()

        // Create the file
        fileFolder.mkdirs()
        file = File(fileFolder, filename)
        file.createNewFile()

        // Open the file for writing
        outputStream = FileOutputStream(file)

        // Write the content to the file
        outputStream.write(noteContent.toByteArray())
        outputStream.flush()

        // Close the output stream
        outputStream.close()

        // Show success message
        Log.d("SaveFile", "File saved successfully!")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("SaveFile", "Error saving file: ${e.message}")
    }
    return file
}

private fun shareFile(fileToShare: File, context: Context) {
    try {
        val authority = "com.rfcreations.animenotepad.provider"
        val noteFileUri =
            FileProvider.getUriForFile(context, authority, fileToShare, fileToShare.name)
        val sendIntent =
            Intent(Intent.ACTION_SEND).apply {
                type = "application/text"
                putExtra(Intent.EXTRA_STREAM, noteFileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        val shareIntent = Intent.createChooser(sendIntent, fileToShare.name)
        context.startActivity(shareIntent)
        Log.d("ShareFile", "Success ${noteFileUri.encodedPath}")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("ShareFile", "Error sharing file: ${e.message}")
    }
}

