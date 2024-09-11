package com.example.submissionintermediate.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class PwCustomView : AppCompatEditText, View.OnTouchListener {

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle:Int):
            super(context, attrs, defStyle){
                init()
            }
    private fun init(){
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8){
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                }else{
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }


}