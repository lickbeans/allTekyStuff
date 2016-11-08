package com.aaroncampbell.budget.Network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class SessionRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (UserStore.getInstance().getToken() != null) {

        }
        return chain.proceed(request);
    }
}
