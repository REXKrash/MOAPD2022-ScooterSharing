package dk.itu.moapd.scootersharing.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.databinding.FragmentScannerBinding
import dk.itu.moapd.scootersharing.viewmodels.ScannerViewModel
import dk.itu.moapd.scootersharing.viewmodels.ScannerViewModelFactory
import kotlinx.coroutines.launch

class ScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding
    private lateinit var viewModel: ScannerViewModel
    private lateinit var codeScanner: CodeScanner

    private var mPermissionGranted = false

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentScannerBinding.inflate(inflater, container, false)
        val view = binding.root

        val scooterDao = AppDatabase.getDatabase(requireActivity().application).scooterDao()
        val viewModelFactory =
            ScannerViewModelFactory(ScooterRepository(scooterDao))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ScannerViewModel::class.java)

        val scannerView = binding.scannerView

        codeScanner = CodeScanner(requireContext(), scannerView)

        codeScanner.camera = CodeScanner.CAMERA_FRONT
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            lifecycleScope.launch {
                viewModel.parseScooter(it.text)
                viewModel.getScooter().observe(viewLifecycleOwner) { scooter ->
                    scooter?.let {
                        findNavController().navigate(
                            ScannerFragmentDirections.actionScannerFragmentToScooterDetailsFragment(
                                scooter.id
                            )
                        )
                    }
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            lifecycleScope.launch {
                Toast.makeText(
                    requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mPermissionGranted = false
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS)
            } else {
                mPermissionGranted = true
            }
        } else {
            mPermissionGranted = true
        }

        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true
                codeScanner.startPreview()
            } else {
                mPermissionGranted = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mPermissionGranted) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}
