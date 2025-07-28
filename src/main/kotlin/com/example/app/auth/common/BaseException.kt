package com.example.app.auth.common

open class BaseException(
  val code: Int?,
  override val message: String
) : RuntimeException(message)