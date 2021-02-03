package com.dy.memorygod.manager

import android.content.Context
import android.provider.ContactsContract
import com.dy.memorygod.data.ContactEmailData
import com.dy.memorygod.data.ContactPhoneNumberData
import com.dy.memorygod.enums.LogType
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.*
import kotlin.collections.ArrayList


object ContactManager {

    private const val ERROR_CONTACT_CURSOR = "ERROR_CONTACT_CURSOR"
    private const val ERROR_CONTACT_CURSOR_NAME = "ERROR_CONTACT_CURSOR_NAME"
    private const val ERROR_CONTACT_CURSOR_PHONE_NUMBER = "ERROR_CONTACT_CURSOR_PHONE_NUMBER"
    private const val ERROR_CONTACT_EMPTY = "ERROR_CONTACT_EMPTY"

    var ERROR_MESSAGE = "ERROR_MESSAGE"
    val ERROR_CONTACT_PHONE_NUMBER = emptyList<ContactPhoneNumberData>()
    private val ERROR_CONTACT_EMAIL = emptyList<ContactEmailData>()

    fun getPhoneNumberList(context: Context): List<ContactPhoneNumberData> {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        val cursor = context.contentResolver.query(
            uri, projection, null,
            null, sortOrder
        )

        if (cursor == null) {
            ERROR_MESSAGE = ERROR_CONTACT_CURSOR
            return ERROR_CONTACT_PHONE_NUMBER
        }

        val list = ArrayList<ContactPhoneNumberData>()
        val nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (cursor.moveToNext()) {
            val name = cursor.getString(nameIdx)
            val phoneNumber = cursor.getString(phoneNumberIdx)

            if (name.isNullOrEmpty()) {
                ERROR_MESSAGE = ERROR_CONTACT_CURSOR_NAME
                return ERROR_CONTACT_PHONE_NUMBER
            }

            if (phoneNumber.isNullOrEmpty()) {
                ERROR_MESSAGE = ERROR_CONTACT_CURSOR_PHONE_NUMBER
                return ERROR_CONTACT_PHONE_NUMBER
            }

            val phoneNumberFormat = getPhoneNumberFormat(context, phoneNumber)
            val data = ContactPhoneNumberData(name, phoneNumberFormat)
            list.add(data)
        }
        cursor.close()

        if (list.size <= 0) {
            ERROR_MESSAGE = ERROR_CONTACT_EMPTY
            return ERROR_CONTACT_PHONE_NUMBER
        }

        return list
    }

    fun getPhoneNumberFormat(context: Context, text: String): String {
        if (text.isEmpty()) {
            return ""
        }

        try {
            val phoneNumberUtil = PhoneNumberUtil.getInstance()
            val locale = Locale.getDefault().country
            val phoneNumber = phoneNumberUtil.parse(text, locale)

            return phoneNumberUtil.format(
                phoneNumber,
                PhoneNumberUtil.PhoneNumberFormat.NATIONAL
            )
        } catch (ex: NumberParseException) {
            val errorMessage = ex.toString()
            val logMessageArr = arrayOf(
                "text : $text",
                "errorMessage : $errorMessage"
            )

            val logType = LogType.PHONE_NUMBER_FORMAT_ERROR
            val logMessage = FirebaseLogManager.getJoinData(logMessageArr)
            FirebaseLogManager.log(context, logType, logMessage)

            return "FORMAT_ERROR"
        }
    }

    fun getEmailList(context: Context): List<ContactEmailData> {
        val uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.DATA
        )
        val filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''"
        val sortOrder = ("CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE")
        val cursor = context.contentResolver.query(
            uri,
            projection,
            filter,
            null,
            sortOrder
        )

        if (cursor == null) {
            ERROR_MESSAGE = ERROR_CONTACT_CURSOR
            return ERROR_CONTACT_EMAIL
        }

        val list = ArrayList<ContactEmailData>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val email = cursor.getString(1)

            val data = ContactEmailData(name, email)
            list.add(data)
        }
        cursor.close()

        return list
    }

}