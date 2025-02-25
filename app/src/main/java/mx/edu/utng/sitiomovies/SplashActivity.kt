package mx.edu.utng.sitiomovies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // Ocultar la barra de estado
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Animar el logo
        val logo = findViewById<ImageView>(R.id.splash_logo)
        val title = findViewById<TextView>(R.id.splash_title)
        val subtitle = findViewById<TextView>(R.id.splash_subtitle)
        val progressBar = findViewById<ProgressBar>(R.id.splash_progress)

        // Animación del logo
        logo.alpha = 0f
        logo.scaleX = 0.3f
        logo.scaleY = 0.3f

        logo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1000)
            .setInterpolator(OvershootInterpolator())
            .start()

        // Animación del título
        title.alpha = 0f
        title.translationY = 50f

        title.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(500)
            .setInterpolator(DecelerateInterpolator())
            .start()

        // Animación del subtítulo
        subtitle.alpha = 0f
        subtitle.translationY = 50f

        subtitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(700)
            .setInterpolator(DecelerateInterpolator())
            .start()

        // Animación de la barra de progreso
        progressBar.alpha = 0f
        progressBar.animate()
            .alpha(1f)
            .setDuration(500)
            .setStartDelay(1200)
            .start()

        // Navegar a MainActivity después de las animaciones
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            // Animación de transición
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }, 3000)
    }
}
