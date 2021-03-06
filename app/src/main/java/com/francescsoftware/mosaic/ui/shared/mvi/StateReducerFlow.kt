package com.francescsoftware.mosaic.ui.shared.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val BufferSize = 64

interface StateReducerFlow<S : State, A : Action> : StateFlow<S>, ActionHandler<A>

fun <S : State, A : Action> ViewModel.stateReducerFlow(
    initialState: S,
    reducer: Reducer<S, A>,
    middleware: List<Middleware<S, A>> = emptyList(),
): StateReducerFlow<S, A> = StateReducerFlowImpl(
    initialState,
    reducer,
    middleware,
    viewModelScope,
)

private class StateReducerFlowImpl<S : State, A : Action>(
    initialState: S,
    reducer: Reducer<S, A>,
    middleware: List<Middleware<S, A>> = emptyList(),
    private val scope: CoroutineScope,
) : StateReducerFlow<S, A> {

    private val actions = MutableSharedFlow<A>(extraBufferCapacity = BufferSize)
    private val reducers = middleware + reducer

    private val stateFlow: StateFlow<S> = actions
        .runningFold(initialState, ::reduce)
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = initialState,
        )

    override val replayCache get() = stateFlow.replayCache

    override val value get() = stateFlow.value

    override suspend fun collect(
        collector: FlowCollector<S>
    ): Nothing {
        stateFlow.collect(collector)
    }

    override fun handleAction(action: A) {
        scope.launch { actions.emit(action) }
    }

    private fun reduce(state: S, action: A): S = reducers.fold(
        initial = state,
        operation = { acc, reducer -> reducer.reduce(acc, action) }
    )
}
