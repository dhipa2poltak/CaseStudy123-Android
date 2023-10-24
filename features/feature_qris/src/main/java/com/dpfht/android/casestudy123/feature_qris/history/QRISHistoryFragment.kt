package com.dpfht.android.casestudy123.feature_qris.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpfht.android.casestudy123.feature_qris.R.layout
import com.dpfht.android.casestudy123.feature_qris.databinding.FragmentQRISHistoryBinding
import com.dpfht.android.casestudy123.framework.commons.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRISHistoryFragment : BaseFragment<FragmentQRISHistoryBinding>(layout.fragment_q_r_i_s_history) {

  private val viewModel by viewModels<QRISHistoryViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    binding.rvTransactions.adapter = viewModel.adapter

    observeViewModel()

    viewModel.start()
  }

  private fun observeViewModel() {
    viewModel.isNoData.observe(viewLifecycleOwner) { isNoData ->
      if (isNoData) {
        binding.rvTransactions.visibility = View.INVISIBLE
        binding.tvNoData.visibility = View.VISIBLE
      } else {
        binding.rvTransactions.visibility = View.VISIBLE
        binding.tvNoData.visibility = View.GONE
      }
    }

    viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
      if (message.isNotEmpty()) {
        navigationService.navigateToErrorMessage(message)
      }
    }
  }
}
