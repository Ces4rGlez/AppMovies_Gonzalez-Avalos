package mx.edu.utng.sitiomovies

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inicializar SharedPreferences antes de llamar a super.onCreate
        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        // Aplicar el tema antes de llamar a super.onCreate y setContentView
        val isDarkMode = sharedPreferences.getBoolean("DarkMode", false)
        val isHighContrast = sharedPreferences.getBoolean("HighContrast", false)

        when {
            isHighContrast -> setTheme(R.style.Theme_SitioMovies_HighContrast)
            isDarkMode -> setTheme(R.style.Theme_SitioMovies_Dark)
            else -> setTheme(R.style.Theme_SitioMovies)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Configurar el switch de Dark Mode
        val switchDarkMode: SwitchMaterial = findViewById(R.id.switchDarkMode)
        switchDarkMode.isChecked = isDarkMode
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().apply {
                putBoolean("DarkMode", isChecked)
                apply()
            }
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            recreate()
        }

        // Configurar el switch de High Contrast
        val switchHighContrast: SwitchMaterial = findViewById(R.id.switchHighContrast)
        switchHighContrast.isChecked = isHighContrast
        switchHighContrast.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().apply {
                putBoolean("HighContrast", isChecked)
                apply()
            }
            recreate()
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualizar el estado de los switches cuando la actividad se reanuda
        findViewById<SwitchMaterial>(R.id.switchDarkMode).isChecked =
            sharedPreferences.getBoolean("DarkMode", false)
        findViewById<SwitchMaterial>(R.id.switchHighContrast).isChecked =
            sharedPreferences.getBoolean("HighContrast", false)
    }
}
