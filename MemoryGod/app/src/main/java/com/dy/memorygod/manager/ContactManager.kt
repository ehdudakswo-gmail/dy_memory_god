package com.dy.memorygod.manager

import android.content.Context
import android.provider.ContactsContract
import com.dy.memorygod.data.ContactData

object ContactManager {

    private const val ERROR_CONTACT_CURSOR = "ERROR_CONTACT_CURSOR"
    private const val ERROR_CONTACT_EMPTY = "ERROR_CONTACT_EMPTY"

    val ERROR_CONTACT = emptyList<ContactData>()
    var ERROR_MESSAGE = "ERROR_MESSAGE"

    fun getContactList(context: Context): List<ContactData> {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        val sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        val cursor = context.contentResolver.query(
            uri, projection, null,
            null, sortOrder
        )

        if (cursor == null) {
            ERROR_MESSAGE = ERROR_CONTACT_CURSOR
            return ERROR_CONTACT
        }

        val list = ArrayList<ContactData>()
        while (cursor.moveToNext()) {
            val phone = cursor.getString(0)
            val name = cursor.getString(1)
            list.add(ContactData(phone, name))
        }
        cursor.close()

        if (list.size <= 0) {
            ERROR_MESSAGE = ERROR_CONTACT_EMPTY
            return ERROR_CONTACT
        }

        return list
    }

}