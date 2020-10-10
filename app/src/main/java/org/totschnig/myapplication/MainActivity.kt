package org.totschnig.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import java.util.*

class MainActivity : AppCompatActivity() {
	private lateinit var manager: SplitInstallManager
	/** Listener used to handle changes in state for install requests. */
	private val listener = SplitInstallStateUpdatedListener { state ->
		Log.d("SPLIT","State "+ state.status())
		val langsInstall = state.languages().isNotEmpty()

		val names = if (langsInstall) {
			// We always request the installation of a single language in this sample
			state.languages().first()
		} else state.moduleNames().joinToString(" - ")

		when (state.status()) {
			SplitInstallSessionStatus.INSTALLED -> {
				onSuccessfulLanguageLoad(names)
			}
		}
	}

	override fun onResume() {
		// Listener can be registered even without directly triggering a download.
		manager.registerListener(listener)
		super.onResume()
	}

	override fun onPause() {
		// Make sure to dispose of the listener once it's no longer needed.
		manager.unregisterListener(listener)
		super.onPause()
	}

	private fun onSuccessfulLanguageLoad(lang: String) {
		LanguageHelper.language = lang
		recreate()
	}

	override fun attachBaseContext(newBase: Context) {
		super.attachBaseContext(newBase)
		applyOverrideConfiguration(LanguageHelper.getLanguageConfiguration())
		SplitCompat.installActivity(this)
	}
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		manager = SplitInstallManagerFactory.create(this)
	}

	fun loadGerman(view: View) {
		if (manager.installedLanguages.contains("de")) {
			onSuccessfulLanguageLoad("de")
			return
		}

		val request = SplitInstallRequest.newBuilder()
				.addLanguage(Locale.GERMAN)
				.build()

		// Load and install the requested language.
		manager.startInstall(request)
	}
}