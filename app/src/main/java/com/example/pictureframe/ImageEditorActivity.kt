package com.example.pictureframe

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.pictureframe.databinding.ActivityImageEditorBinding

class ImageEditorActivity : AppCompatActivity(), onClick {

    private lateinit var binding: ActivityImageEditorBinding

    private lateinit var list: MutableList<FrameList>
    private val frameItem = arrayOf(R.drawable.frame2,R.drawable.frame3,R.drawable.frame4,R.drawable.frame5,R.drawable.frame6,R.drawable.frame7,R.drawable.frame8,R.drawable.frame9,R.drawable.frame10,R.drawable.frame11,R.drawable.frame12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImageEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        list = ArrayList()
        val path = intent.getStringExtra("path")

        // Toast.makeText(this,"path: $path", Toast.LENGTH_SHORT).show()
        Glide.with(this@ImageEditorActivity).load("$path").placeholder(R.drawable.img).into(binding.userImage)

        initList()
        binding.frameRecycler.adapter = FrameAdapter(this, list, this)

        binding.button.setOnClickListener {
            storeImage(getScreenshot(binding.screenshotView))
        }
    }

    private fun initList() {
        for (i in frameItem){
            list.add(FrameList(i))
        }
    }

    override fun frameClick(position: Int) {
        Glide.with(this).load(list[position].drawableFrame).into(binding.frameContainer)
    }

    private fun getScreenshot(view: View) : Bitmap{

        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.getDrawingCache())
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    private fun storeImage(bitmap: Bitmap){
        var uri : Uri? = null

        if(SDK_INT >= Build.VERSION_CODES.R){
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }
        else{
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "display.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)

        }

        val u = contentResolver.insert(uri, contentValues)

        try {
            val outputStream = contentResolver.openOutputStream(u!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
            Toast.makeText(this,"Image Saved", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this,"Fail to Save", Toast.LENGTH_SHORT).show()
        }
    }
}