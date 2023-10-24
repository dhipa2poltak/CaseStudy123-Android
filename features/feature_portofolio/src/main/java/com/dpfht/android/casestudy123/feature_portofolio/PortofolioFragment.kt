package com.dpfht.android.casestudy123.feature_portofolio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpfht.android.casestudy123.feature_portofolio.R.layout
import com.dpfht.android.casestudy123.feature_portofolio.adapter.TransactionAdapter
import com.dpfht.android.casestudy123.feature_portofolio.databinding.FragmentPortofolioBinding
import com.dpfht.android.casestudy123.framework.commons.base.BaseFragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortofolioFragment : BaseFragment<FragmentPortofolioBinding>(layout.fragment_portofolio) {

  private val viewModel by viewModels<PortofolioViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    observeViewModel()
    setListener()

    viewModel.start()
  }

  private fun observeViewModel() {
    viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
      if (message.isNotEmpty()) {
        navigationService.navigateToErrorMessage(message)
      }
    }

    viewModel.pieEntries.observe(viewLifecycleOwner) { pieEntries ->
      val dataSet = PieDataSet(pieEntries, "")
      val data = PieData(dataSet)
      binding.pieChart.data = data
      dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
      binding.pieChart.description.text = ""
      binding.pieChart.legend.isEnabled = false
      binding.pieChart.invalidate()
    }
  }

  private fun setListener() {
    binding.pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
      override fun onValueSelected(e: Entry?, h: Highlight?) {
        val pe = e as PieEntry
        val str = "${pe.label} (${pe.value}%)"
        binding.tvTitle.text = str

        viewModel.trxChartEntities?.get(0)?.let {
          val trxDetails = it.data[pe.data as Int].data
          val adapter = TransactionAdapter(trxDetails)
          binding.rvTransactions.adapter = adapter
          binding.rvTransactions.visibility = View.VISIBLE
        }
      }

      override fun onNothingSelected() {
        binding.tvTitle.text = ""
        binding.rvTransactions.visibility = View.INVISIBLE
      }
    })
  }
}
