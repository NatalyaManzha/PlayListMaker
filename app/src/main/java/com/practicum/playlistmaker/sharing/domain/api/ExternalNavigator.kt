package com.practicum.playlistmaker.sharing.domain.api

interface ExternalNavigator {
    fun shareLink(url:String)
    fun openUserAgreement(url:String)
    fun writeToSupport(subject:String, message:String, email: String)

}