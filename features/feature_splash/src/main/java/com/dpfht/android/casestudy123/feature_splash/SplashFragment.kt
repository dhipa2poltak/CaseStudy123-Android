package com.dpfht.android.casestudy123.feature_splash

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.dpfht.android.casestudy123.feature_splash.databinding.FragmentSplashBinding
import com.dpfht.android.casestudy123.framework.commons.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

  private val viewModel by viewModels<SplashViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    observeViewModel()
    viewModel.start()
  }

  private fun observeViewModel() {
    viewModel.isInitializationDone.observe(viewLifecycleOwner) { isDone ->
      if (isDone) {
        navigationService.navigateToHome()
      }
    }
  }

  override fun onStart() {
    super.onStart()
    (requireActivity() as AppCompatActivity).supportActionBar?.hide()
  }

  override fun onStop() {
    super.onStop()
    (requireActivity() as AppCompatActivity).supportActionBar?.show()
  }
}
