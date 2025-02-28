package com.example.myplayer.data.repo

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.myplayer.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class StopPlayerWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "StopPlayerWorker"

        /** 调度定时任务 */
        fun schedule(context: Context, minute: Int) {
            val request = OneTimeWorkRequestBuilder<StopPlayerWorker>().run {
                setInitialDelay(minute.toLong(), TimeUnit.MINUTES)
                build()
            }
            WorkManager.getInstance(context).enqueue(request)
        }

        /** 取消任务 */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWork()
        }
    }

    override suspend fun doWork(): Result {
        try {
            withContext(Dispatchers.Main) {
                (applicationContext as MyApplication).player.stop()
            }
            Log.i(TAG, "doWork: player stopped")
        } catch (e: Exception) {
            Log.e(TAG, "doWork: stop failed", e)
        }
        return Result.success()
    }
}