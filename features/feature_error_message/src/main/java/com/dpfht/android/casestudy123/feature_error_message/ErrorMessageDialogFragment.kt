package com.dpfht.android.casestudy123.feature_error_message

import android.os.Bundle
import android.view.View
import com.dpfht.android.casestudy123.feature_error_message.databinding.FragmentErrorMessageDialogBinding
import com.dpfht.android.casestudy123.framework.Constants
import com.dpfht.android.casestudy123.framework.commons.base.BaseBottomSheetDialogFragment

class ErrorMessageDialogFragment : BaseBottomSheetDialogFragment<FragmentErrorMessageDialogBinding>(R.layout.fragment_error_message_dialog) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    arguments?.let {
      val message = it.getString(Constants.FragmentArgsName.ARG_MESSAGE)
      binding.tvMessage.text = message
    }

    setListener()
  }

  private fun setListener() {
    binding.btnOk.setOnClickListener {
      dismiss()
    }
  }
}
