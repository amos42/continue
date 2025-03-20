package com.github.continuedev.continueeclipseextension.utils;

import java.util.Timer;
import java.util.TimerTask;

public class Debouncer {
    private final long interval;
    private final Timer timer;
    private TimerTask debounceTask;

    public Debouncer(long interval) {
        this.interval = interval;
        this.timer = new Timer();
    }

    public void debounce(Runnable action) {
        if (debounceTask != null) {
            debounceTask.cancel();
        }

        debounceTask = new TimerTask() {
            @Override
            public void run() {
                action.run();
            }
        };

        timer.schedule(debounceTask, interval);
    }

    public void stop() {
        if (debounceTask != null) {
            debounceTask.cancel();
        }
        timer.cancel();
    }
}