package com.dpfht.android.casestudy123.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.dpfht.android.casestudy123.framework.navigation.NavigationService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NavigationServiceTest {

  private lateinit var navigationService: NavigationService

  private lateinit var navController: NavController
  private lateinit var activity: AppCompatActivity

  @Before
  fun setup() {
    navController = mock()
    activity = mock()

    navigationService = NavigationServiceImpl(navController, activity)
  }

  @Test
  fun `ensure navigate method is called in navController when calling navigateToQRCodeScannerForResult method in navigationService`() {
    navigationService.navigateToQRCodeScannerForResult()

    verify(navController).navigate(any<NavDeepLinkRequest>())
  }

  @Test
  fun `ensure navigate method is called in navController when calling navigateToQRISHistory method in navigationService`() {
    navigationService.navigateToQRISHistory()

    verify(navController).navigate(any<NavDeepLinkRequest>())
  }

  @Test
  fun `ensure navigate method is called in navController when calling navigateToPortofolio method in navigationService`() {
    navigationService.navigateToPortofolio()

    verify(navController).navigate(any<NavDeepLinkRequest>())
  }

  @Test
  fun `ensure navigate method is called in navController when calling navigateToErrorMessage method in navigationService`() {
    navigationService.navigateToErrorMessage("this is an error message")

    verify(navController).navigate(any<NavDeepLinkRequest>())
  }

  @Test
  fun `ensure navigateUp method is called in navController when calling navigateUp method in navigationService`() {
    navigationService.navigateUp()

    verify(navController).navigateUp()
  }
}
