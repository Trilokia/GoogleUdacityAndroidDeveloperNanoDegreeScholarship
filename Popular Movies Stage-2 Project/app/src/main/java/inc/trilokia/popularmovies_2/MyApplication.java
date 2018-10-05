package inc.trilokia.popularmovies_2;

import android.app.Application;

import inc.trilokia.popularmovies_2.api.NetworkModule;


public class MyApplication extends Application {

    private NetworkComponent networkComponent;
    private static MyApplication instance;
//https://stackoverflow.com/questions/17441295/android-context-without-being-in-an-activity-and-other-activity-less-programmin
    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        networkComponent = DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule())
                .build();
    }

    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }

}