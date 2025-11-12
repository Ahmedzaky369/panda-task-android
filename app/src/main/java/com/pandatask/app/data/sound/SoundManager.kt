package com.pandatask.app.data.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*

/**
 * Manages sound effects for the app
 * Replicates the Web Audio API sounds from the PWA
 */
@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = ContextCompat.getSystemService(context, VibratorManager::class.java)
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            ContextCompat.getSystemService(context, Vibrator::class.java)
        }
    }
    
    suspend fun playSound(type: SoundType, enabled: Boolean = true) {
        if (!enabled) return
        
        withContext(Dispatchers.IO) {
            when (type) {
                SoundType.COMPLETE -> generateTone(440.0, 200)
                SoundType.ADD -> generateTone(550.0, 150)
            }
        }
        
        // Add subtle vibration
        vibrate(when (type) {
            SoundType.COMPLETE -> 50
            SoundType.ADD -> 30
        })
    }
    
    private fun generateTone(frequency: Double, durationMs: Int) {
        try {
            val sampleRate = 44100
            val samples = (sampleRate * durationMs / 1000.0).toInt()
            val buffer = ShortArray(samples)
            
            // Generate sine wave
            for (i in 0 until samples) {
                val time = i.toDouble() / sampleRate
                val amplitude = (0.1 * Short.MAX_VALUE).toInt()
                val envelope = exp(-time * 5) // Exponential decay like PWA
                buffer[i] = (amplitude * envelope * sin(2 * PI * frequency * time)).toInt().toShort()
            }
            
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setSampleRate(sampleRate)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()
            
            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            
            // Auto-cleanup after playing
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                audioTrack.stop()
                audioTrack.release()
            }, (durationMs + 50).toLong())
            
        } catch (e: Exception) {
            // Fallback to vibration only if audio fails
            vibrate(100)
        }
    }
    
    private fun vibrate(durationMs: Long) {
        vibrator?.let { vib ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(
                    VibrationEffect.createOneShot(
                        durationMs,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(durationMs)
            }
        }
    }
}

enum class SoundType {
    COMPLETE,
    ADD
}