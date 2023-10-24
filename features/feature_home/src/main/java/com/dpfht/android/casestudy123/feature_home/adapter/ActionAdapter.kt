package com.dpfht.android.casestudy123.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dpfht.android.casestudy123.feature_home.R
import com.dpfht.android.casestudy123.feature_home.databinding.LayoutCellActionBinding
import com.dpfht.android.casestudy123.feature_home.model.ActionVWModel

class ActionAdapter(
  context: Context,
  private val actionModels: ArrayList<ActionVWModel>
): ArrayAdapter<ActionVWModel>(context, R.layout.layout_cell_action, actionModels) {

  var onClickActionCallback: ((Int) -> Unit)? = null

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val binding = if (convertView == null) {
      LayoutCellActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    } else {
      convertView.tag as LayoutCellActionBinding
    }

    binding.root.tag = binding

    val model = actionModels[position]
    binding.ivAction.setImageResource(model.imageId)
    binding.tvAction.text = model.title

    binding.root.setOnClickListener {
      onClickActionCallback?.let { it(position) }
    }

    return binding.root
  }
}
