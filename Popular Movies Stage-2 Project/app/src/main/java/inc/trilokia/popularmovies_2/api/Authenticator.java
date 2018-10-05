package inc.trilokia.popularmovies_2.api;

import java.io.IOException;

import inc.trilokia.popularmovies_2.MyApplication;
import inc.trilokia.popularmovies_2.utils.PrefUtils;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class Authenticator implements Interceptor{

PrefUtils prefUtils;

    @Override
    public Response intercept(Chain chain) throws IOException {

//https://stackoverflow.com/questions/17441295/android-context-without-being-in-an-activity-and-other-activity-less-programmin
        prefUtils = new PrefUtils(MyApplication.getInstance());
        Request oldRequest = chain.request();

        HttpUrl oldUrl = oldRequest.url();
        HttpUrl newUrl = oldUrl.newBuilder()

                .addQueryParameter("api_key", prefUtils.getApiKey())
                .build();

        Request newRequest = oldRequest.newBuilder()
                .url(newUrl)
                .build();

        return chain.proceed(newRequest);
    }


}