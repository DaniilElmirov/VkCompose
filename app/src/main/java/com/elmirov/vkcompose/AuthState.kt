package com.elmirov.vkcompose

sealed interface AuthState {

    object Initial : AuthState

    object Authorized : AuthState

    object NoAuthorized : AuthState
}