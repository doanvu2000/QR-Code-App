package com.dd.company.qrapp.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.TypedValue
import androidx.core.content.ContextCompat

fun Context.getColorWithAttr(resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

fun Context.getDrawableByName(name: String): Int =
    resources.getIdentifier(name, "drawable", packageName)

fun Context.sendEmail(isContact: Boolean = true) {
    val myVersion: String = Build.VERSION.RELEASE
//    val sdkVersion: String = BuildConfig.VERSION_NAME
    val intentSendEmail = Intent(Intent.ACTION_SEND)
    intentSendEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf("EMAIL_SUPPORT_FEEDBACK"))
//    val typeEmail = if (isContact) R.string.subject_contact_question else R.string.feedback
//    intentSendEmail.putExtra(
//        Intent.EXTRA_SUBJECT,
//        getString(
//            typeEmail,
//            getString(R.string.app_name), getString(R.string.name_operating)
//        )
//    )
//    intentSendEmail.putExtra(
//        Intent.EXTRA_TEXT,
//        "\n\n\n\n[Android: $myVersion, ${getString(R.string.ios_name_of_device_mail_signature)}" +
//                ": ${getDeviceName()}, ${getString(R.string.version_of_application_mail_signature)}" +
//                ": $sdkVersion]"
//    )
    intentSendEmail.type = "plain/text"
    startActivity(intentSendEmail)
}

fun Context.isGrantedPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.openAppSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}
