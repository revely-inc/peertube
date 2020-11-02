package co.revely.peertube.di

import android.app.Application
import android.content.Context
import co.revely.peertube.utils.AppExecutors
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.*
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import org.commonmark.parser.Parser
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.Executors


/**
 * Created at 2019-10-11
 *
 * @author rbenjami
 */
val appModule = module {
	single {
		val app = get<Application>()
		app.getSharedPreferences(app.packageName, Context.MODE_PRIVATE)
	}

	single {
		AppExecutors()
	}

	single { Markwon
		.builder(get())
		.usePlugin(LinkifyPlugin.create())
		.usePlugin(HtmlPlugin.create())
		.build()
	}

	single { Parser.builder().build() }
}
