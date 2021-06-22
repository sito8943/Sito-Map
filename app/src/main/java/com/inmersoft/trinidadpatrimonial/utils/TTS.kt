package com.inmersoft.trinidadpatrimonial.utils

import android.app.Activity
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.*

class TTS(
    private val activity: Activity,
    private val message: String,
    private val es: Boolean
) : TextToSpeech.OnInitListener {

    private val tts: TextToSpeech = TextToSpeech(activity, this)

    override fun onInit(i: Int) {
        if (i == TextToSpeech.SUCCESS) {

            val localeBR = Locale("es", "ES")
            val localeUS = Locale.US
            val result: Int = if (es) tts.setLanguage(localeBR) else tts.setLanguage(localeUS)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(activity, "This Language is not supported", Toast.LENGTH_SHORT)
                    .show()
            } else {
                speakOut(message)
            }
        } else {
            Toast.makeText(activity, "Initialization Failed!", Toast.LENGTH_SHORT).show()
        }
    }



    private fun speakOut(message: String) {
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun isSpeaking(): Boolean {
        return tts.isSpeaking
    }

    fun shutdown() {
        tts.shutdown()
    }


}