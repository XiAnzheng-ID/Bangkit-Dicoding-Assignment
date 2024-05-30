package com.devin.storykw.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText

class PasswordCustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {
    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pw = s.toString()
                if (pw.length < 8 && pw.isNotEmpty()) {
                    setError("Password harus minimal 8 karakter", null)
                } else {
                    setError(null, null)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
}