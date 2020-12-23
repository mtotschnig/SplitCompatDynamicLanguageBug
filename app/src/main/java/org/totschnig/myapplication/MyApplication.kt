/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.totschnig.myapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.google.android.play.core.splitcompat.SplitCompat
import java.util.Locale

/** We have to use a custom Application class, because we want to
 * initialize the selected language before SplitCompat#install() has a chance to run.
 */
class MyApplication : Application() {

    override fun attachBaseContext(base: Context) {
        LanguageHelper.init(base)
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}

internal const val LANG_EN = "en"

private const val PREFS_LANG = "language"

/**
 * A singleton helper for storing and retrieving the user selected language in a
 * SharedPreferences instance. It is required for persisting the user language choice between
 * application restarts.
 */
object LanguageHelper {
    lateinit var prefs: SharedPreferences
    var language: String
        get() {
            return prefs.getString(PREFS_LANG, LANG_EN)!!
        }
        set(value) {
            prefs.edit().putString(PREFS_LANG, value).apply()
        }

    fun init(ctx: Context) {
        prefs = ctx.getSharedPreferences(PREFS_LANG, Context.MODE_PRIVATE)
    }

    /**
     * Get an empty Configuration instance that only sets the language that is
     * stored in the LanguageHelper preferences.
     * For use with Context#createConfigurationContext or Activity#applyOverrideConfiguration().
     */
    fun getLanguageConfiguration(configuration: Configuration): Configuration {
        configuration.setLocale(Locale.forLanguageTag(language))
        return configuration
    }
}
