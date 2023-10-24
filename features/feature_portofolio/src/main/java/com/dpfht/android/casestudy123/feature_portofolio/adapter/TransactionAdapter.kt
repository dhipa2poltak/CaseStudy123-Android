package com.dpfht.android.casestudy123.feature_portofolio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dpfht.android.casestudy123.feature_portofolio.adapter.TransactionAdapter.ViewHolder
import com.dpfht.android.casestudy123.feature_portofolio.databinding.LayoutRowTransactionBinding
import com.dpfht.android.casestudy123.framework.ext.toRupiahString
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxDetailsEntity

class TransactionAdapter(
  private val data: List<TrxDetailsEntity>
): RecyclerView.Adapter<ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = LayoutRowTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val details = data[position]

    holder.binding.tvDate.text = details.trxDate
    holder.binding.tvNominal.text = details.nominal.toRupiahString()
  }

  override fun getItemCount(): Int {
    return data.size
  }

  class ViewHolder(val binding: LayoutRowTransactionBinding): RecyclerView.ViewHolder(binding.root)
}
