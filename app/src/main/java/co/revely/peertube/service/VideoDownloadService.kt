package co.revely.peertube.service

import android.app.Notification
import co.revely.peertube.R
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.util.NotificationUtil
import com.google.android.exoplayer2.util.Util
import org.koin.android.ext.android.inject

/**
 * Created at 2019-10-14
 *
 * @author rbenjami
 */
class VideoDownloadService : DownloadService(
		FOREGROUND_NOTIFICATION_ID,
		DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
		CHANNEL_ID,
		R.string.exo_download_notification_channel_name,
		0
)
{
	companion object
	{
		private const val CHANNEL_ID = "download_channel"
		private const val JOB_ID = 1
		private const val FOREGROUND_NOTIFICATION_ID = 1

		private var nextNotificationId = FOREGROUND_NOTIFICATION_ID + 1
	}

	private val mDownloadManager: DownloadManager by inject()

	private val notificationHelper: DownloadNotificationHelper by lazy {
		DownloadNotificationHelper(this, CHANNEL_ID)
	}

	init
	{
		nextNotificationId = FOREGROUND_NOTIFICATION_ID + 1
	}

	override fun getDownloadManager() = mDownloadManager

	override fun getScheduler(): PlatformScheduler?
	{
		return if (Util.SDK_INT >= 21) PlatformScheduler(this, JOB_ID) else null
	}

	override fun getForegroundNotification(downloads: List<Download>): Notification =
		notificationHelper.buildProgressNotification(R.drawable.ic_download, null, null, downloads)

	override fun onDownloadChanged(download: Download)
	{
		val notification: Notification = when
		{
			download.state == Download.STATE_COMPLETED -> notificationHelper.buildDownloadCompletedNotification(
					R.drawable.ic_download_done, null,
					Util.fromUtf8Bytes(download.request.data)
			)
			download.state == Download.STATE_FAILED -> notificationHelper.buildDownloadFailedNotification(
					R.drawable.ic_download_done, null,
					Util.fromUtf8Bytes(download.request.data)
			)
			else -> return
		}
		NotificationUtil.setNotification(this, nextNotificationId++, notification)
	}
}