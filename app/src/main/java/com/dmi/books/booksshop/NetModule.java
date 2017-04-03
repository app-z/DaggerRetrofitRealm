package com.dmi.books.booksshop;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    String mBaseUrl;
    String credentials = Credentials.basic("usertest", "secret");
    HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();

    public NetModule(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }


    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    /*
    public class FakeInterceptor implements Interceptor {
        // FAKE RESPONSES.
        private final static String BOOKS_ = "{\"id\":\"7\",\"title\":\"Mock Seven is my lucky number\",\"price\":7.77,\"link\":\"/api/v1/items/7\"},{\"id\":\"8\",\"title\":\"A Dance with Dragons\",\"price\":19.01,\"link\":\"/api/v1/items/8\"},{\"id\":\"10\",\"title\":\"Ten ways to a better mind\",\"price\":10.0,\"link\":\"/api/v1/items/10\"},{\"id\":\"42\",\"title\":\"The Hitch-hikers Guide to the Galaxy\",\"price\":5.62,\"link\":\"/api/v1/items/42\"},{\"id\":\"200\",\"title\":\"Book title #200\",\"price\":84.0,\"link\":\"/api/v1/items/200\"},{\"id\":\"201\",\"title\":\"Book title #201\",\"price\":75.0,\"link\":\"/api/v1/items/201\"},{\"id\":\"202\",\"title\":\"Book title #202\",\"price\":22.0,\"link\":\"/api/v1/items/202\"},{\"id\":\"203\",\"title\":\"Book title #203\",\"price\":57.0,\"link\":\"/api/v1/items/203\"},{\"id\":\"204\",\"title\":\"Book title #204\",\"price\":63.0,\"link\":\"/api/v1/items/204\"},{\"id\":\"205\",\"title\":\"Book title #205\",\"price\":63.0,\"link\":\"/api/v1/items/205\"}";

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = null;
            if(BuildConfig.DEBUG) {
                String responseString;
                // Get Request URI.
                final URI uri = chain.request().url().uri();
                // Get Query String.
                final String query = uri.getQuery();
                // Parse the Query String.
                final String[] parsedQuery = query.split("=");
                responseString = BOOKS_;

                response = new Response.Builder()
                        .code(200)
                        .message(responseString)
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                        .addHeader("content-type", "application/json")
                        .build();
            }
            else {
                response = chain.proceed(chain.request());
            }

            return response;
        }

    }
*/

    @Provides
    @Named("okhttp_mock")
    @Singleton
    OkHttpClient provideOkHttpClientMock(Cache cache) {

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                // FAKE RESPONSES.
                final String BOOKS_ = "[{\"id\":\"7\",\"title\":\"Mock Seven is my lucky number\",\"price\":7.77,\"link\":\"/api/v1/items/7\"},{\"id\":\"8\",\"title\":\"A Dance with Dragons\",\"price\":19.01,\"link\":\"/api/v1/items/8\"},{\"id\":\"10\",\"title\":\"Ten ways to a better mind\",\"price\":10.0,\"link\":\"/api/v1/items/10\"},{\"id\":\"42\",\"title\":\"The Hitch-hikers Guide to the Galaxy\",\"price\":5.62,\"link\":\"/api/v1/items/42\"},{\"id\":\"200\",\"title\":\"Book title #200\",\"price\":84.0,\"link\":\"/api/v1/items/200\"},{\"id\":\"201\",\"title\":\"Book title #201\",\"price\":75.0,\"link\":\"/api/v1/items/201\"},{\"id\":\"202\",\"title\":\"Book title #202\",\"price\":22.0,\"link\":\"/api/v1/items/202\"},{\"id\":\"203\",\"title\":\"Book title #203\",\"price\":57.0,\"link\":\"/api/v1/items/203\"},{\"id\":\"204\",\"title\":\"Book title #204\",\"price\":63.0,\"link\":\"/api/v1/items/204\"},{\"id\":\"205\",\"title\":\"Book title #205\",\"price\":63.0,\"link\":\"/api/v1/items/205\"}]";
                Response response = null;
                String responseString;
                // Get Request URI.
                final URI uri = chain.request().url().uri();
                // Get Query String.
                final String query = uri.getQuery();
                // Parse the Query String.
                final String[] parsedQuery = query.split("=");
                responseString = BOOKS_;

                response = new Response.Builder()
                        .code(200)
                        .message(responseString)
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                        .addHeader("content-type", "application/json")
                        .build();
                return response;
            }
        };

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);
        return client.build();
    }

    @Provides
    @Named("okhttp")
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache) {

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        credentials);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        };

        if (BuildConfig.DEBUG)
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);
        client.addInterceptor(logInterceptor);
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, @Named("okhttp_mock") OkHttpClient okhttp) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okhttp)
                .build();
    }
}
