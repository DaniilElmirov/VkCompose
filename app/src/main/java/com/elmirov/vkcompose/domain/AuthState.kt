package com.elmirov.vkcompose.domain

sealed interface AuthState {

    object Initial : AuthState

    object Authorized : AuthState

    object NoAuthorized : AuthState
}