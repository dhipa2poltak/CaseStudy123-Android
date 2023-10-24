package com.dpfht.android.casestudy123.feature_qris.transaction

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.dpfht.android.casestudy123.feature_qris.R
import com.dpfht.android.casestudy123.feature_qris.R.layout
import com.dpfht.android.casestudy123.feature_qris.databinding.FragmentQRISTransactionBinding
import com.dpfht.android.casestudy123.framework.commons.base.BaseBottomSheetDialogFragment
import com.dpfht.android.casestudy123.framework.commons.model.QRCodeValue
import com.dpfht.android.casestudy123.framework.commons.model.toDomain
import com.dpfht.android.casestudy123.framework.ext.toRupiahString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRISTransactionFragment : BaseBottomSheetDialogFragment<FragmentQRISTransactionBinding>(layout.fragment_q_r_i_s_transaction) {

  private val viewModel by viewModels<QRISTransactionViewModel>()

  var qrCode = ""
  var onDoneCallback: (() -> Unit)? = null
  private var qrCodeValue: QRCodeValue? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initViewData()
    observeViewModel()
    setListener()
  }

  private fun initViewData() {
    binding.tvMessageForm.visibility = View.INVISIBLE
    binding.tvBalanceForm.visibility = View.INVISIBLE

    if (qrCode.isNotEmpty()) {
      val arr = qrCode.split(".")
      if (arr.size == 4) {
        qrCodeValue = QRCodeValue(
          source = arr[0],
          idTransaction = arr[1],
          merchantName = arr[2],
          nominal = arr[3].toDoubleOrNull()
        )

        qrCodeValue?.let {
          binding.tvMerchantNameValue.text = it.merchantName
          binding.tvNominalValue.text = it.nominal?.toRupiahString()
          binding.tvIdTransaksiValue.text = it.idTransaction
        }
      }
    }
  }

  private fun observeViewModel() {
    viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
      if (message.isNotEmpty()) {
        binding.tvMessageForm.text = message
        binding.tvMessageForm.visibility = View.VISIBLE
        binding.tvBalanceForm.text = ""
        binding.tvBalanceForm.visibility = View.INVISIBLE
      }
    }

    viewModel.transactionState.observe(viewLifecycleOwner) { pairState ->
      if (pairState.first) {
        binding.clTransactionForm.visibility = View.INVISIBLE
        binding.clTransactionSuccess.visibility = View.VISIBLE

        val msg = resources.getString(R.string.qris_text_transaction_success)
        binding.tvMessageSuccess.text = msg

        val msgBalance = String.format(resources.getString(R.string.qris_text_ending_balance), pairState.second.toRupiahString())
        binding.tvBalanceSucess.text =  msgBalance
      } else {
        val msg = resources.getString(R.string.qris_text_not_enough_balance)
        binding.tvMessageForm.text = msg
        binding.tvMessageForm.visibility = View.VISIBLE

        val msgBalance = String.format(resources.getString(R.string.qris_text_your_balance), pairState.second.toRupiahString())
        binding.tvBalanceForm.text = msgBalance
        binding.tvBalanceForm.visibility = View.VISIBLE
      }
    }
  }

  private fun setListener() {
    binding.ivClose.setOnClickListener {
      dismiss()
      if (viewModel.isDone) {
        onDoneCallback?.let { it() }
      }
    }

    binding.btnPay.setOnClickListener {
      binding.tvMessageForm.visibility = View.INVISIBLE
      binding.tvBalanceForm.visibility = View.INVISIBLE

      binding.tvMessageForm.text = ""
      binding.tvBalanceForm.text = ""

      if (qrCodeValue != null) {
        qrCodeValue?.let {
          viewModel.postQRISTransaction(it.toDomain())
        }
      }
    }

    binding.btnDone.setOnClickListener {
      dismiss()
      onDoneCallback?.let { it() }
    }
  }

  companion object {
    fun newInstance(): QRISTransactionFragment {
      return QRISTransactionFragment()
    }
  }
}
