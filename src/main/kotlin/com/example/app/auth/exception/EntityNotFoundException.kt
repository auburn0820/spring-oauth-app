package com.example.app.auth.exception

import com.example.app.auth.common.BaseException

class EntityNotFoundException(
  code: Int? = null,
  message: String
) : BaseException(
  code = code,
  message = message
)