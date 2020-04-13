package com.dy.memorygod.manager

import android.content.Context
import android.provider.ContactsContract
import com.dy.memorygod.data.ContactEmailData
import com.dy.memorygod.data.ContactPhoneNumberData

object ContactManager {

    private const val ERROR_CONTACT_CURSOR = "ERROR_CONTACT_CURSOR"
    private const val ERROR_CONTACT_EMPTY = "ERROR_CONTACT_EMPTY"
    private const val PHONE_NUMBER_SEPARATOR = "-"

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
        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            var phoneNumber = cursor.getString(1)
            phoneNumber = phoneNumber.replace(PHONE_NUMBER_SEPARATOR, "")

            val data = ContactPhoneNumberData(name, phoneNumber)
            list.add(data)
        }
        cursor.close()

        if (list.size <= 0) {
            ERROR_MESSAGE = ERROR_CONTACT_EMPTY
            return ERROR_CONTACT_PHONE_NUMBER
        }

        return list
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