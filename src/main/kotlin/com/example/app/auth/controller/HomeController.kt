package com.example.app.auth.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
  @GetMapping("/")
  fun home(): String {
    return "Welcome to the Home Page!"
  }
}