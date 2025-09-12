package com.dpfht.casestudy123.domain.model

sealed class VoidResult {
  object Success: VoidResult()
  data class Error(val message: String): VoidResult()
}
