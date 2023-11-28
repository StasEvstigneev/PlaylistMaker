package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonReturn = findViewById<ImageView>(R.id.return_from_settings)
        buttonReturn.setOnClickListener {
            this.finish()
        }

        val nightThemeSwitcher = findViewById<SwitchMaterial>(R.id.switcherNightTheme)
        nightThemeSwitcher.isChecked = (applicationContext as App).nightTheme
        nightThemeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).switchNightTheme(isChecked)
            (applicationContext as App).settingsSharedPrefs.edit().putBoolean(NIGHT_THEME, isChecked).apply()
        }


        val buttonShare = findViewById<ImageView>(R.id.ivShareSettings)
        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.url_android_developer))
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        val buttonSupport = findViewById<ImageView>(R.id.ivSupportSettings)
        buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_to_support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.message_to_support_text))
            }
            startActivity(Intent.createChooser(supportIntent, null))
        }

        val buttonUserAgreement = findViewById<ImageView>(R.id.ivUserAgreementSettings)
        buttonUserAgreement.setOnClickListener {
            val userAgreement: Uri = Uri.parse(getString(R.string.url_user_agreement))
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, userAgreement)
            startActivity(userAgreementIntent)
        }

    }
}