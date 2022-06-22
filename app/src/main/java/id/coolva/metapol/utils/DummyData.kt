package id.coolva.metapol.utils

import id.coolva.metapol.core.domain.model.User

object DummyData {

    fun provideUserList(): List<User> {
        val userList = ArrayList<User>()
        userList.add(
            User(
                name = "Jamal Akhyer",
                email = "alif.akbarkartadinata2049@students.unila.ac.id",
                password = "uvuvwevwevwe",
                phoneNumber = null,
                profilePhoto = null
            )
        )

        userList.add(
            User(
                name = "Muhammad Wafa",
                email = "muhammadwafa105@gmail.com",
                password = "passwordmetapol",
                phoneNumber = "081271306749",
                profilePhoto = null
            )
        )

        return userList
    }
}