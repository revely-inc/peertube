package co.revely.peertube.di

import android.app.Application
import android.content.Context
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.utils.DownloadTracker
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.*
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import java.io.File


/**
 * Created at 2019-10-11
 *
 * @author rbenjami
 */
inline fun <reified T> Scope.getWithParams(vararg params: Any?) = this.get<T>(parameters = { parametersOf(*params) })

val appModule = module {
	single {
		val app = get<Application>()
		app.getSharedPreferences(app.packageName, Context.MODE_PRIVATE)
	}

	single {
		AppExecutors()
	}

	single<DatabaseProvider> { ExoDatabaseProvider(get()) }
	single<WritableDownloadIndex> { DefaultDownloadIndex(get()) }
	single(named("download_cache")) {
		fun  getDownloadDirectory(context: Context): File? {
			var downloadDirectory = context.getExternalFilesDir(null)
			if (downloadDirectory == null)
				downloadDirectory = context.filesDir
			return downloadDirectory
		}

		val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
		val downloadContentDirectory = File(getDownloadDirectory(get()), DOWNLOAD_CONTENT_DIRECTORY)
		SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), get<DatabaseProvider>())
	}
	single(named("user_agent")) { Util.getUserAgent(get(), get<Application>().packageName) }
	single { DefaultHttpDataSourceFactory(get(named("user_agent"))) }
	single { DefaultDataSourceFactory(get(), get<DefaultHttpDataSourceFactory>()) }
	single { CacheDataSourceFactory(
			get<SimpleCache>(named("download_cache")),
			get<DefaultDataSourceFactory>(),
			FileDataSource.Factory(),
			null,
			CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
			null)
	}
	single { DownloaderConstructorHelper(get<SimpleCache>(named("download_cache")), get<DefaultHttpDataSourceFactory>()) }
	single { DefaultDownloaderFactory(get()) }
	single { DownloadManager(get(), get(), get<DefaultDownloaderFactory>()) }
	single { DownloadTracker(get(), get<CacheDataSourceFactory>(), get()) }
}
