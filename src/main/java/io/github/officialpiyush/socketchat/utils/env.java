package io.github.officialpiyush.socketchat.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class env {
    public static String get(String key) {
        Dotenv env = Dotenv.load();
        return env.get(key);
    }
}
