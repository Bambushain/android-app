package app.bambushain

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import app.bambushain.api.AuthenticationApi
import app.bambushain.api.CalendarApi
import app.bambushain.api.CharactersApi
import app.bambushain.api.CraftersApi
import app.bambushain.api.CustomFieldsApi
import app.bambushain.api.FightersApi
import app.bambushain.api.FreeCompaniesApi
import app.bambushain.api.FreeCompanyApi
import app.bambushain.api.GatherersApi
import app.bambushain.api.GrovesApi
import app.bambushain.api.HousingApi
import app.bambushain.api.LegalApi
import app.bambushain.api.MyProfileApi
import app.bambushain.api.PandasApi
import app.bambushain.api.R
import app.bambushain.api.SupportApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

fun Context.getBambooPrefs(): SharedPreferences? =
    this.getSharedPreferences("bamboo", Context.MODE_PRIVATE)

fun Context.getBambooToken(): String? {
    return getBambooPrefs()?.getString("bamboo_token", null)
}

fun Context.setBambooToken(token: String) {
    getBambooPrefs()?.edit {
        putString("bamboo_token", token)
    }
}

val apiModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    factory {
        val context = androidContext()
        val server = context.getString(R.string.server)
        Retrofit
            .Builder()
            .baseUrl(server)
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor {
                        it.proceed(
                            it
                                .request()
                                .newBuilder()
                                .addHeader(
                                    "Authorization",
                                    "Bearer ${androidContext().getBambooToken()}"
                                )
                                .build()
                        )
                    }
                    .build())
            .build()
    }
    factory {
        get<Retrofit>().create(AuthenticationApi::class.java)
    }
    factory {
        get<Retrofit>().create(CalendarApi::class.java)
    }
    factory {
        get<Retrofit>().create(CharactersApi::class.java)
    }
    factory {
        get<Retrofit>().create(CraftersApi::class.java)
    }
    factory {
        get<Retrofit>().create(CustomFieldsApi::class.java)
    }
    factory {
        get<Retrofit>().create(FightersApi::class.java)
    }
    factory {
        get<Retrofit>().create(FreeCompaniesApi::class.java)
    }
    factory {
        get<Retrofit>().create(FreeCompanyApi::class.java)
    }
    factory {
        get<Retrofit>().create(GatherersApi::class.java)
    }
    factory {
        get<Retrofit>().create(GrovesApi::class.java)
    }
    factory {
        get<Retrofit>().create(HousingApi::class.java)
    }
    factory {
        get<Retrofit>().create(LegalApi::class.java)
    }
    factory {
        get<Retrofit>().create(MyProfileApi::class.java)
    }
    factory {
        get<Retrofit>().create(PandasApi::class.java)
    }
    factory {
        get<Retrofit>().create(SupportApi::class.java)
    }
}
