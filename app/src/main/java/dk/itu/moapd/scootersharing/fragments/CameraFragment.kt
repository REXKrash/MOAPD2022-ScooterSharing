package dk.itu.moapd.scootersharing.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.databinding.FragmentCameraBinding
import dk.itu.moapd.scootersharing.viewmodels.CameraViewModel
import dk.itu.moapd.scootersharing.viewmodels.CameraViewModelFactory
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var viewModel: CameraViewModel

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var imageUri: Uri? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val args: CameraFragmentArgs by navArgs()

    companion object {
        private val TAG = CameraFragment::class.qualifiedName
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory =
            CameraViewModelFactory(args.scooterId, ScooterRepository(requireActivity().application))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CameraViewModel::class.java)

        if (allPermissionsGranted())
            startCamera()
        else
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )

        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cameraSwitchButton.let {
            it.isEnabled = false
            it.setOnClickListener {
                cameraSelector = if (CameraSelector.DEFAULT_FRONT_CAMERA == cameraSelector)
                    CameraSelector.DEFAULT_BACK_CAMERA
                else
                    CameraSelector.DEFAULT_FRONT_CAMERA

                startCamera()
            }
        }
        outputDirectory = getOutputDirectory(args.scooterId)
        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS)
            if (allPermissionsGranted())
                startCamera()
            else {
                toast("Permissions not granted by the user.")
            }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

                updateCameraSwitchButton(cameraProvider)
            } catch (ex: Exception) {
                Log.e(TAG, "Use case binding failed", ex)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            "latestScooter.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    imageUri = Uri.fromFile(photoFile)
                    imageUri?.let { imageUri ->
                        viewModel.updateScooterImage(imageUri)
                        val msg = "Photo capture succeeded: $imageUri"
                        toast(msg)
                        Log.d(TAG, msg)
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
            }
        )
    }

    private fun updateCameraSwitchButton(provider: ProcessCameraProvider) {
        try {
            binding.cameraSwitchButton.isEnabled =
                hasBackCamera(provider) && hasFrontCamera(provider)
        } catch (exception: CameraInfoUnavailableException) {
            binding.cameraSwitchButton.isEnabled = false
        }
    }

    private fun hasBackCamera(provider: ProcessCameraProvider) =
        provider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)

    private fun hasFrontCamera(provider: ProcessCameraProvider) =
        provider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)

    private fun getOutputDirectory(scooterId: Int): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + File.separator + scooterId).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    private fun toast(
        text: CharSequence,
        duration: Int = Toast.LENGTH_SHORT,
    ) {
        Toast.makeText(requireContext(), text, duration).show()
    }
}
