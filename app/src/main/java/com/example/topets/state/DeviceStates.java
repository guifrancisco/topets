package com.example.topets.state;

import java.net.HttpURLConnection;

public class DeviceStates {
    public enum RegistrationState{
        /**
         * The device was already registered. Everything is fine
         */
        ALREADY_REGISTERED(HttpURLConnection.HTTP_OK),
        /**
         * The device was not registered, but now is. Everything is fine
         */
        REGISTERED(HttpURLConnection.HTTP_CREATED),
        /**
         * The device was not registered, and the registration failed. Everything is not fine
         */
        NOT_REGISTERED(0);

        final int httpResponseCode;

        RegistrationState(int httpResponseCode) {
            this.httpResponseCode = httpResponseCode;
        }

        public static RegistrationState valueOf(int v){
            switch (v){
                case HttpURLConnection.HTTP_OK:
                    return ALREADY_REGISTERED;
                case HttpURLConnection.HTTP_CREATED:
                    return REGISTERED;
                default:
                    return NOT_REGISTERED;
            }
        }
    }
}
