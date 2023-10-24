package com.dpfht.android.casestudy123.feature_home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.dpfht.android.casestudy123.feature_home.adapter.ActionAdapter
import com.dpfht.android.casestudy123.feature_home.databinding.FragmentHomeBinding
import com.dpfht.android.casestudy123.feature_home.model.ActionVWModel
import com.dpfht.android.casestudy123.framework.commons.base.BaseFragment
import com.dpfht.android.casestudy123.framework.ext.toRupiahString
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

  companion object {
    const val QRIS_POSITION = 0
    const val HISTORY_POSITION = 1
    const val CHART_POSITION = 2

    const val COUNT_FIELD_QR_CODE = 4
  }

  private val actionModels = arrayListOf<ActionVWModel>()

  private val viewModel by viewModels<HomeViewModel>()

  private lateinit var actionAdapter: ActionAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)

    actionModels.clear()
    actionModels.add(ActionVWModel(R.drawable.ic_home_qr_code_scanner, resources.getString(R.string.home_text_qris)))
    actionModels.add(ActionVWModel(R.drawable.ic_home_history, resources.getString(R.string.home_text_history)))
    actionModels.add(ActionVWModel(R.drawable.ic_home_pie_chart, resources.getString(R.string.home_text_chart)))

    actionAdapter = ActionAdapter(requireContext(), actionModels)
    actionAdapter.onClickActionCallback = this::onClickAction
    binding.gvAction.adapter = actionAdapter

    observeViewModel()
    setListener()

    viewModel.start()
  }

  private fun observeViewModel() {
    viewModel.balance.observe(viewLifecycleOwner) { balance ->
      binding.tvBalance.text = balance.toRupiahString()
    }

    viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
      if (message.isNotEmpty()) {
        navigationService.navigateToErrorMessage(message)
      }
    }

    viewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
      binding.swRefreshHome.isRefreshing = isRefreshing
    }
  }

  private fun setListener() {
    binding.swRefreshHome.setOnRefreshListener {
      viewModel.start()
    }
  }

  private fun onClickAction(position: Int) {
    when (position) {
      QRIS_POSITION -> {
        scanQRCode()
      }
      HISTORY_POSITION -> {
        navigationService.navigateToQRISHistory()
      }
      CHART_POSITION -> {
        navigationService.navigateToPortofolio()
      }
    }
  }

  private fun scanQRCode() {
    val options = ScanOptions()
    options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
    options.setPrompt(resources.getString(R.string.home_text_scan_qr_code))
    options.setCameraId(0) // Use a specific camera of the device
    options.setBarcodeImageEnabled(true)
    options.setOrientationLocked(false)
    options.setBeepEnabled(true)
    barcodeLauncher.launch(options)
  }

  private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
    if (result.contents == null) {
      Toast.makeText(requireContext(), resources.getString(R.string.home_text_cancel), Toast.LENGTH_LONG).show()
    } else {
      val qrCode = result.contents
      if (isValidQRCode(qrCode)) {
        navigationService.navigateToQRISTransaction(qrCode, this::onDoneQRISTransaction)
      } else {
        navigationService.navigateToErrorMessage(resources.getString(R.string.home_text_invalid_code))
      }
    }
  }

  private fun isValidQRCode(qrCode: String): Boolean {
    val arr = qrCode.split(".")

    if (arr.size != COUNT_FIELD_QR_CODE) {
      return false
    }

    if (arr.any { it.isEmpty() }) {
      return false
    }

    return true
  }

  private fun onDoneQRISTransaction() {
    viewModel.start()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.home_menu, menu)

    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.itm_reset_all_data) {
      viewModel.resetAllData()
      return true
    }
    return super.onOptionsItemSelected(item)
  }
}
