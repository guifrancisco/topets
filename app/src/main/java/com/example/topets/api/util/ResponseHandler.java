package com.example.topets.api.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ResponseHandler {
    /**
     * Handle requests where the <code>isSuccessfull</code> flag is set to false.
     */
    public static void handleFailure(Response<ResponseBody> response) {
        try (ResponseBody errorBody = response.errorBody()){

            if(errorBody == null){
                Log.e(ResponseHandler.class.getSimpleName(), "Null error body when handling failed response.");
                return;
            }

            String errorString = errorBody.string();
            Log.e(ResponseHandler.class.getSimpleName(), String.format("Api response : %s",errorString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
