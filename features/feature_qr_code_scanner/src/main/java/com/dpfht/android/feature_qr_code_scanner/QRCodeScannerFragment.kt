package com.dpfht.android.feature_qr_code_scanner

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import com.dpfht.android.casestudy123.framework.Constants
import com.dpfht.android.casestudy123.framework.commons.base.BaseFragment
import com.dpfht.android.feature_qr_code_scanner.analyzer.QrCodeAnalyzer
import com.dpfht.android.feature_qr_code_scanner.databinding.FragmentQRCodeScannerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class QRCodeScannerFragment : BaseFragment<FragmentQRCodeScannerBinding>(R.layout.fragment_q_r_code_scanner) {

  private lateinit var cameraExecutor: ExecutorService

  private var dialogPerm: AlertDialog? = null
  private var isAlerting = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    cameraExecutor = Executors.newSingleThreadExecutor()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    cameraExecutor.shutdown()
  }

  override fun onResume() {
    super.onResume()
    if (allPermissionsGranted(requireContext(), arrayOf(Manifest.permission.CAMERA))) {
      startCamera()
    } else {
      makeRequestCameraPermission()
    }
  }

  private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

    cameraProviderFuture.addListener({
      val cameraProvider = cameraProviderFuture.get()

      val preview = Preview.Builder().build()
      preview.setSurfaceProvider(binding.previewView.surfaceProvider)

      val imageAnalyzer = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .also {
          val analyzer = QrCodeAnalyzer()
          analyzer.scannedQRCodeCallback = this::onScannedQRCode
          it.setAnalyzer(cameraExecutor, analyzer)
        }

      val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

      try {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageAnalyzer)
      } catch (exc: Exception) {
        exc.printStackTrace()
      }
    }, ContextCompat.getMainExecutor(requireContext()))
  }

  private fun onScannedQRCode(code: String) {
    val bundle = Bundle()
    bundle.putString(Constants.FragmentArgsName.ARG_CODE, code)
    setFragmentResult(Constants.FragmentActionKeys.ACTION_KEY_QR_CODE, bundle)
    navigationService.navigateUp()
  }

  private fun makeRequestCameraPermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
      val builder = AlertDialog.Builder(requireContext())
      builder.setMessage(resources.getString(R.string.msg_please_allow)).setTitle(resources.getString(R.string.msg_permission_camera))
      builder.setPositiveButton(resources.getString(R.string.text_ok)) {
          _, _ ->

        makeRequestCamera()
      }

      val dialog = builder.create()
      dialog.show()
    } else {
      makeRequestCamera()
    }
  }

  private fun makeRequestCamera() {
    if (!isAlerting) {
      askCameraPermission.launch(Manifest.permission.CAMERA)
    }
  }

  private val askCameraPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
      if (allPermissionsGranted(requireContext(), arrayOf(Manifest.permission.CAMERA))) {
        startCamera()
      } else {
        showErrorAskingCameraPermission()
      }
    }

  private fun allPermissionsGranted(activityContext: Context, permissions: Array<String>) = permissions.all {
    ContextCompat.checkSelfPermission(activityContext, it) == PackageManager.PERMISSION_GRANTED
  }

  private fun openSettings(activityContext: Context, packageName: String) {
    val itn = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    itn.data = uri
    activityContext.startActivity(itn)
  }

  private fun showErrorAskingCameraPermission() {
    val builder = AlertDialog.Builder(requireContext())
    builder.setMessage(resources.getString(R.string.msg_not_granted)).setTitle(resources.getString(R.string.text_title_perm))
    builder.setPositiveButton(resources.getString(R.string.text_close)) {
        _, _ ->

      dialogPerm?.dismiss()
      isAlerting = false
      navigationService.navigateUp()
    }
    builder.setNegativeButton(resources.getString(R.string.text_open_settings)) {
        _, _ ->

      dialogPerm?.dismiss()
      isAlerting = false
      openSettings(requireActivity(), requireContext().packageName)
    }
    builder.setOnDismissListener {
      dialogPerm = null
      isAlerting = false
    }

    builder.setCancelable(false)
    dialogPerm = builder.create()
    dialogPerm?.show()

    isAlerting = true
  }
}
