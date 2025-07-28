package com.example.app.auth.common

data class ApiResponse<T>(
  val code: Int,
  val message: String? = null,
  val data: T? = null,
)