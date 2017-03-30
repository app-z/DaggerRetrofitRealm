# DaggerRetrofitRealm
Dagger 2, Retrofit 2, Realm 3

This project about conjunction three framework torgether.

Realm is written on native code and if properly included as a plugin it will not significantly increase apk

https://realm.io/news/android-installation-change/

Using the plugin we can now ship Realm as an AAR, as opposed to a JAR. We can also avoid including the annotation processor in the library and make it a standalone package. This means the your final APKs will not have to include the annotation processor, shaving a few kilobytes off your app.

Another useful library from Square is the Dagger. Dagger 2 uses code generation, in contrast to the reflection in the first version.
Now about Dagger. There are many articles, but I will not enumerate for a long time and copy everything in detail. This article will help the beginner understand how to organize the structure of modules and make injections.
To create the Activity objects Retrofit and Realm you need two lines of code

```
@Inject
Retrofit retrofit;
@Inject
Realm mRealm;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
	...
	((App) getApplication()).getApplicationComponent().inject(this);
  ```

Now look at ApplicationComponent. Dagger generates the names of class names. For example, DaggerApplicationComponent from ApplicationComponent, Realm from provideRealm, ...
  
```
public class App extends Application {
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
```
Here we created our modules. More precisely, Dagger created them for us. How does the generation occur?
Dager uses the Anotation

```
@Module
public class RealmModule {
    @Provides
    @Singleton
    Realm provideRealm(RealmConfiguration realmConfiguration) {
		...

```

@Singleton provideRealm becomes @Inject Realm mRealm

Similarly occurs in the NetModule
