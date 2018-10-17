package inc.trilokia.bakingapp.app;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;


public class BakingApp extends Application {
    private static BakingApp instance;
    private Gson gson;
    private EventBus eventBus;

    public BakingApp() {
        instance = this;
    }

    public static BakingApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createGson();
        createEventBus();
    }

    private void createEventBus() {
        eventBus = EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .build();
    }

    private void createGson() {
        gson = new GsonBuilder().create();
    }

    public Gson getGson() {
        return gson;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
