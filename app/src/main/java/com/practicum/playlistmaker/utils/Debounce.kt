package com.practicum.playlistmaker.utils


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/** Когда вызывается эта функция, она возвращает новую функцию, которую можно вызывать с любым параметром типа T.
 * @param delayMillis задержка выполнения в миллисекундах
 * @param coroutineScope область выполнения корутины
 * @param useLastParam == true - каждый новый вызов будет отложен и только последний выполнится
 * @param useLastParam == false - выполнится только первый запрос, последующие будут игнорироваться, пока первый не завершится
 * @param action функция, вызов которой осуществляется с вышеописанными условиями
 */
fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}