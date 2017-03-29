package com.dmi.books.booksshop;

import android.app.Application;

public class App extends Application {

    private static String WEB_SERVICE_BASE_URL;
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        WEB_SERVICE_BASE_URL = "http://" + AppUtils.getNetworkHost(this);

        mApplicationComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(WEB_SERVICE_BASE_URL))
                .realmModule(new RealmModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
