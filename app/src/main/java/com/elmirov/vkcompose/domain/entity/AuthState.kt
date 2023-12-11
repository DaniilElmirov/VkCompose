package com.elmirov.vkcompose.domain.entity

sealed interface AuthState {

    object Initial : AuthState

    object Authorized : AuthState

    object NoAuthorized : AuthState
}