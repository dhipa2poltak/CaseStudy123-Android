package com.dpfht.casestudy123.domain.entity

sealed class VoidResult {
  object Success: VoidResult()
  data class Error(val message: String): VoidResult()
}
