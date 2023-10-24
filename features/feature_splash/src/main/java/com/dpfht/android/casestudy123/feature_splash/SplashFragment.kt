package com.dpfht.android.casestudy123.feature_splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.dpfht.android.casestudy123.feature_splash.databinding.FragmentSplashBinding
import com.dpfht.android.casestudy123.framework.Constants
import com.dpfht.android.casestudy123.framework.commons.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

  private val viewModel by viewModels<SplashViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    observeViewModel()
    viewModel.start()

    Handler(Looper.getMainLooper()).postDelayed({
      viewModel.onDelayDone()
      navigateToHome()
    }, Constants.DELAY_SPLASH)
  }

  private fun observeViewModel() {
    viewModel.isInitDBDoneData.observe(viewLifecycleOwner) { isDone ->
      if (isDone) {
        navigateToHome()
      }
    }
  }

  private fun navigateToHome() {
    if (viewModel.isDelayDone && viewModel.isInitDBDone) {
      navigationService.navigateToHome()
    } else if (viewModel.isDelayDone) {
      binding.tvLoading.text = requireContext().resources.getString(R.string.splash_init_db)
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
