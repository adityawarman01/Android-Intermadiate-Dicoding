package com.example.submissionintermediate.view.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.submissionintermediate.MainActivity
import com.example.submissionintermediate.R
import com.example.submissionintermediate.ViewModelFactory
import com.example.submissionintermediate.database.getImageUri
import com.example.submissionintermediate.database.reduceFileImage
import com.example.submissionintermediate.database.repository.ResultState
import com.example.submissionintermediate.database.uriToFile
import com.example.submissionintermediate.databinding.ActivityAddStoryBinding

class AddStory : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "permission request denied", Toast.LENGTH_LONG).show()
            }
        }


    private fun allPermessionsGranted() =
        ContextCompat.checkSelfPermission(this,
            REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.new_story)


        if (!allPermessionsGranted()){
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.cameraButton.setOnClickListener { startCamera() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadStory() }
    }
    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){
        uri: Uri? ->
            if (uri != null){
                currentImageUri = uri
                showImage()
            }else{
                Log.d("photo picker", "No media selected")
            }
       }

    private val launchCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess){
            showImage()
        }
    }

    private fun startCamera(){
        currentImageUri = getImageUri(this)
        launchCamera.launch(currentImageUri)
    }

    private fun showImage(){
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it ")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadStory(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("the file", "showImage: ${imageFile.path}")
            val description = binding.edtDesc.text.toString()

            viewModel.uploadStory(imageFile, description).observe(this) {result ->
                if (result != null){
                    when(result){
                        is ResultState.Success ->{
                            Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
                            showLoading(false)

                            val intent = Intent(this@AddStory, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                        is ResultState.Error ->{
                            Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressIndicator.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}