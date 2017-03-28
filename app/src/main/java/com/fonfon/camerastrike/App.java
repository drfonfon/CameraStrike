package com.fonfon.camerastrike;

import android.app.Application;

import com.fonfon.camerastrike.lib.GameType;

public final class App extends Application {

    public String code = null;
    public GameType gameType = GameType.None;
    public String nodename = null;

    private static App app;
    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
