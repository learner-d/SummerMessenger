package com.summermessenger.ui.register

import com.summermessenger.data.model.User

data class RegisterResult(val success: User? = null, val error: Any? = null)