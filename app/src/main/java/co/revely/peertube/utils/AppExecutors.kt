package co.revely.peertube.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
open class AppExecutors(
	private val diskIO: Executor = Executors.newSingleThreadExecutor(),
	private val networkIO: Executor = Executors.newFixedThreadPool(3),
	private val mainThread: Executor = MainThreadExecutor()
)
{
	fun diskIO() = diskIO
	fun networkIO() = networkIO
	fun mainThread() = mainThread

	private class MainThreadExecutor : Executor
	{
		private val mainThreadHandler = Handler(Looper.getMainLooper())
		override fun execute(command: Runnable)
		{
			mainThreadHandler.post(command)
		}
	}
}