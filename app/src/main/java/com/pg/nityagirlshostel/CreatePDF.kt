package com.pg.nityagirlshostel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@RequiresApi(Build.VERSION_CODES.Q)
suspend fun createPdfFromUser(context: Context, user: User) {
    withContext(Dispatchers.IO) {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 15f
                isAntiAlias = true
            }

            var y = 40f
            val centerX = pageInfo.pageWidth / 2

            // 1️⃣ Profile Image (top and center)
            user.image_url?.let { url ->
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .build()
                val result = context.imageLoader.execute(request)
                val drawable = result.drawable
                val bitmap = drawable?.toBitmap(config = Bitmap.Config.ARGB_8888)

                bitmap?.let {
                    val scaled = Bitmap.createScaledBitmap(it, 140, 150, true)
                    val x = centerX - (scaled.width / 2)
                    canvas.drawBitmap(scaled, x.toFloat(), y, null)
                    y += 170f
                }
            }

            // 2️⃣ User Info Text (middle)
            val lines = listOf(
                "Adhar no: ${user.adhar_no}",
                "Student Name : ${user.name ?: "N/A"}",
                "Father Name : ${user.father ?: "N/A"}",
                "Mother Name : ${user.mother ?: "N/A"}",
                "Mobile Name: ${user.mobile ?: "N/A"}",
                "Email : ${user.email ?: "N/A"}",
                "Permanent Address : ${user.per_address ?: "N/A"}",
                "Hostel Joined Date : ${user.join_date ?: "N/A"}"
            )

            lines.forEach {
                canvas.drawText(it, 40f, y, paint)
                y += 25f
            }

            y += 20f // Small spacer before Aadhaar

            // 3️⃣ Aadhaar Front and Back Side-by-Side
//            val aadhaarImageWidth = 250
//            val aadhaarImageHeight = 190
//            val spacingBetweenImages = 20
//            val margin = 40f

            val adharY = y // Fixed y for both images

// Aadhaar Front (Left)
            user.adhar_image?.let { url ->
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .build()
                val result = context.imageLoader.execute(request)
                val drawable = result.drawable
                val bitmap = drawable?.toBitmap(config = Bitmap.Config.ARGB_8888)

                bitmap?.let {
                    val scaled = Bitmap.createScaledBitmap(it, 250, 190, true)
                    canvas.drawBitmap(scaled, 40f, adharY, null) // Draw on left
                }
            }

// Aadhaar Back (Right)
            user.adhar_back?.let { url ->
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .build()
                val result = context.imageLoader.execute(request)
                val drawable = result.drawable
                val bitmap = drawable?.toBitmap(config = Bitmap.Config.ARGB_8888)

                bitmap?.let {
                    val scaled = Bitmap.createScaledBitmap(it, 250, 190, true)
                    val rightX = 40f + 250 + 20
                    canvas.drawBitmap(scaled, rightX, adharY, null) // Draw on right
                }
            }

            y += 190 + 40f // Move Y below the images

            pdfDocument.finishPage(page)

            // 📁 Save to Downloads
            val fileName = "${user.name ?: "user"}.pdf"
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                resolver.openOutputStream(it)?.use { outStream ->
                    pdfDocument.writeTo(outStream)
                }
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(it, contentValues, null, null)

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "PDF saved to Downloads as $fileName", Toast.LENGTH_LONG).show()
                }
            }

            pdfDocument.close()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
            e.printStackTrace()
        }
    }
}
