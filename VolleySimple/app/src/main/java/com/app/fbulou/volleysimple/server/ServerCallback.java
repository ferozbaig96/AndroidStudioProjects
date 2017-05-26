package com.app.fbulou.volleysimple.server;

import com.android.volley.VolleyError;

/**
 * Callback Interface used for handling Server API response
 */

public interface ServerCallback<T> {
    void onAPIResponse(String apiTag, T response);

    void onErrorResponse(String apiTag, VolleyError error);
}
