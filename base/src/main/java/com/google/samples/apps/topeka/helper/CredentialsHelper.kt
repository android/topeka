package com.google.samples.apps.topeka.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.support.annotation.VisibleForTesting
import android.util.Log
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsOptions
import com.google.android.gms.common.api.ResolvableApiException
import com.google.samples.apps.topeka.model.Avatar
import com.google.samples.apps.topeka.model.Player

/**
 * Help with signing in and out via SmartLock for Passwords Api.
 */

const val REQUEST_LOGIN = 704
const val REQUEST_SAVE = 54

const val PLAYER_PREFERENCES = "playerPreferences"
const val PREFERENCE_FIRST_NAME = "$PLAYER_PREFERENCES.firstName"
const val PREFERENCE_LAST_INITIAL = "$PLAYER_PREFERENCES.lastInitial"
const val PREFERENCE_AVATAR = "$PLAYER_PREFERENCES.avatar"
const val PREFERENCE_EMAIL = "$PLAYER_PREFERENCES.email"

/**
 * Request login info from Google SmartLock Api.
 */
fun Activity.requestLogin(success: (Player) -> Unit) {
    if (isLoggedIn()) {
        success(getPlayer()!!)
        return
    }
    credentialsApiClient()
            .request(CredentialRequest.Builder()
                    .setPasswordLoginSupported(true)
                    .build())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val player = Player(it.result.credential)
                    storePlayerLocally(player)
                    success(player)
                } else {
                    resolveException(it.exception, REQUEST_LOGIN)
                }
            }
}

/**
 * Save login info using Google SmartLock Api.
 */
fun Activity.saveLogin(player: Player, success: () -> Unit) {
    storePlayerLocally(player)
    credentialsApiClient()
            .save(player.toCredential())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    success()
                } else {
                    resolveException(it.exception, REQUEST_SAVE)
                }
            }
}

/**
 * Resolve exceptions throughout different processes
 */
private fun Activity.resolveException(exception: Exception?, requestCode: Int) {
    if (exception is ResolvableApiException) {
        Log.d(TAG, "Resolving: $exception")
        try {
            exception.startResolutionForResult(this, requestCode)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "STATUS: Failed to send resolution.", e)
        }
    }
}

/**
 * Handles the activity result logic when a call to the SmartLock API is made.
 * Hook this into onActivityResult for maximum benefit.
 */
fun Activity.onSmartLockResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        success: (Player) -> Unit,
        failure: () -> Unit) {
    if (resultCode != Activity.RESULT_OK) {
        return
    }
    when (requestCode) {
        REQUEST_LOGIN -> {
            data?.run {
                if (hasExtra(Credential.EXTRA_KEY)) {
                    val player = Player(getParcelableExtra(Credential.EXTRA_KEY)).also {
                        storePlayerLocally(it)
                    }
                    success(player)
                } else {
                    failure()
                }
            }
        }
        REQUEST_SAVE -> (Log.d(TAG, "savePlayer result"))
    }
}

private fun Context.credentialsApiClient() =
        Credentials.getClient(this, CredentialsOptions.Builder().forceEnableSaveDialog().build())

fun Context.logout() {
    editPlayer().clear().commit()
    database().reset()
}

fun Context.isLoggedIn(): Boolean {
    return with(getPlayerPreferences()) {
        contains(PREFERENCE_FIRST_NAME)
                && contains(PREFERENCE_LAST_INITIAL)
                && contains(PREFERENCE_AVATAR)
    }
}

@SuppressLint("CommitPrefEdits")
private fun Context.editPlayer() = getPlayerPreferences().edit()

private fun Context.getPlayerPreferences() =
        getSharedPreferences(PLAYER_PREFERENCES, Context.MODE_PRIVATE)

@VisibleForTesting
fun Context.getPlayer(): Player? {
    return with(getPlayerPreferences()) {
        val player = Player(getString(PREFERENCE_FIRST_NAME, null),
                getString(PREFERENCE_LAST_INITIAL, null),
                getString(PREFERENCE_AVATAR, null)
                        ?.let { Avatar.valueOf(it) },
                getString(PREFERENCE_EMAIL, ""))
        if (player.valid()) {
            player
        } else {
            null
        }
    }
}

@VisibleForTesting
fun Context.storePlayerLocally(player: Player) {
    with(player) {
        if (valid())
            editPlayer()
                    .putString(PREFERENCE_FIRST_NAME, firstName)
                    .putString(PREFERENCE_LAST_INITIAL, lastInitial)
                    .putString(PREFERENCE_AVATAR, avatar?.name)
                    .putString(PREFERENCE_EMAIL, email)
                    .apply()
    }
}

/**
 * Enable login and storing of a user's credential.
 */
interface Login {
    fun loginPlayer(activity: Activity, onSuccess: (Player) -> Unit)
    fun savePlayer(activity: Activity, player: Player, onSuccess: () -> Unit)
}

/**
 * The default means of obtaining user credentials.
 */
object DefaultLogin : Login {
    override fun loginPlayer(activity: Activity, onSuccess: (Player) -> Unit) {
        activity.requestLogin(onSuccess)
    }

    override fun savePlayer(activity: Activity, player: Player, onSuccess: () -> Unit) {
        activity.saveLogin(player, onSuccess)
    }
}

/**
 * The login implementation used for this app.
 */
var login: Login = DefaultLogin