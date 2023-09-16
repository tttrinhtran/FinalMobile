package com.example.finalproject.RegisterScreen

class EmailValidator {
    companion object {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"

        fun isValidEmail(email: String): Boolean {
            return email.matches(emailRegex.toRegex())
        }

        fun emailNormalization(email: String):String {
            return email.lowercase();
        }
    }
}