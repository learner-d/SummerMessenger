package com.summermessenger.ui.login
/**
 * Data validation state of the login form.
 */
data class TelLoginFormState(val phoneNumError: Int? = null,
                          val msgCodeError: Int? = null,
                          val isPhoneNumValid:Boolean = false,
                          val isMsgCodeValid: Boolean = false)