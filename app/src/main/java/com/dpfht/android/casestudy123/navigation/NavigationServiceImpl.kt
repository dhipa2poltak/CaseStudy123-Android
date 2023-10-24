package com.dpfht.android.casestudy123.navigation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.dpfht.android.casestudy123.R
import com.dpfht.android.casestudy123.feature_qris.transaction.QRISTransactionFragment
import com.dpfht.android.casestudy123.framework.Constants
import com.dpfht.android.casestudy123.framework.navigation.NavigationService

class NavigationServiceImpl(
  private val navController: NavController,
  private val activity: AppCompatActivity
): NavigationService {

  override fun navigateToHome() {
    val navGraph = navController.navInflater.inflate(R.navigation.home_nav_graph)
    navController.graph = navGraph
  }

  override fun navigateToQRISTransaction(qrCode: String, onDoneCallback: (() -> Unit)?) {
    val fragment = QRISTransactionFragment.newInstance()
    fragment.qrCode = qrCode
    fragment.onDoneCallback = onDoneCallback

    fragment.show(activity.supportFragmentManager, "QRIS_TRANSACTION")
  }

  override fun navigateToQRISHistory() {
    val builder = Uri.Builder()
    builder.scheme("android-app")
      .authority("com.dpfht.android.casestudy123")
      .appendPath("qris_history_fragment")

    navController.navigate(
      NavDeepLinkRequest.Builder
        .fromUri(builder.build())
        .build())
  }

  override fun navigateToPortofolio() {
    val builder = Uri.Builder()
    builder.scheme("android-app")
      .authority("com.dpfht.android.casestudy123")
      .appendPath("portofolio_fragment")

    navController.navigate(
      NavDeepLinkRequest.Builder
        .fromUri(builder.build())
        .build())
  }

  override fun navigateToErrorMessage(message: String) {
    val builder = Uri.Builder()
    builder.scheme("android-app")
      .authority("com.dpfht.android.casestudy123")
      .appendPath("error_message_dialog_fragment")
      .appendQueryParameter(Constants.FragmentArgsName.ARG_MESSAGE, message)

    navController.navigate(
      NavDeepLinkRequest.Builder
        .fromUri(builder.build())
        .build())
  }
}
