package com.dmi.books.booksshop;

import com.dmi.books.booksshop.UI.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class, RealmModule.class})
public interface ApplicationComponent {
    void inject(MainActivity activity);
}
