package com.example.pictureframe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pictureframe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val permission = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkPermissions();

        binding.btnSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK){
            val uri = data!!.data

            val filepath = arrayOf(MediaStore.Images.Media.DATA)

            val content = contentResolver.query(uri!!, filepath, null, null, null)
            content!!.moveToNext()

            val columnIndex = content.getColumnIndex(filepath[0])
            val picturePath = content.getString(columnIndex)
            content.close()

            val intent = Intent(this@MainActivity, ImageEditorActivity::class.java)
            intent.putExtra("path", picturePath)
            startActivity(intent)

        }

    }
    private fun checkPermissions(): Boolean {

        var result: Int
        val listPermissionNeeded: MutableList<String> = ArrayList()

        for (p in permission) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result == PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(p)
            }

        }

        if (listPermissionNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toTypedArray(), 0)
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this@MainActivity, "First allow all permissions", Toast.LENGTH_SHORT)
                    .show()
            }

            checkPermissions()
        }
    }

}