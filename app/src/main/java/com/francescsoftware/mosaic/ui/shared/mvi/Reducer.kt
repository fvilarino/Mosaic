package com.francescsoftware.mosaic.ui.shared.mvi

interface Reducer<S : State, A : Action> {
    fun reduce(state: S, action: A): S
}
