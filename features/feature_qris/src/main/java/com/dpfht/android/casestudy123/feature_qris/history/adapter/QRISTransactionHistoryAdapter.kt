package com.dpfht.android.casestudy123.feature_qris.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dpfht.android.casestudy123.feature_qris.databinding.LayoutRowQrisTransactionBinding
import com.dpfht.android.casestudy123.feature_qris.history.adapter.QRISTransactionHistoryAdapter.ViewHolder
import com.dpfht.android.casestudy123.framework.ext.toRupiahString
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class QRISTransactionHistoryAdapter @Inject constructor(

): RecyclerView.Adapter<ViewHolder>() {

  lateinit var qrisTransactionEntities: ArrayList<QRISTransactionEntity>

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = LayoutRowQrisTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val entity = qrisTransactionEntities[position]

    holder.binding.tvDate.text = ""
    entity.transactionDateTime?.let {
      val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
      holder.binding.tvDate.text = formatter.format(it)
    }

    holder.binding.tvMerchant.text = entity.merchantName
    holder.binding.tvIdTransaction.text = entity.idTransaction
    holder.binding.tvNominal.text = entity.nominal.toRupiahString()
  }

  override fun getItemCount(): Int {
    return qrisTransactionEntities.size
  }

  class ViewHolder(val binding: LayoutRowQrisTransactionBinding): RecyclerView.ViewHolder(binding.root)
}
