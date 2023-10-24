package com.dpfht.android.casestudy123.framework.navigation

interface NavigationService {

  fun navigateToHome()
  fun navigateToQRISTransaction(qrCode: String, onDoneCallback: (() -> Unit)?)
  fun navigateToQRISHistory()
  fun navigateToPortofolio()
  fun navigateToErrorMessage(message: String)
}
