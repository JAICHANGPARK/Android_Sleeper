package com.dreamwalker.sleeper.Utils;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 2E313JCP on 2017-10-24.
 */

public class RegisterRequest extends StringRequest {

    private static final String URL = "http://121.187.72.125/UserRegister.php";
    private Map<String, String> param;

    public RegisterRequest(String userID, String userPassword, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        param = new HashMap<>();
        param.put("userID", userID);
        param.put("userPassword", userPassword);
        param.put("userEmail", userEmail);
    }

    @Override
    public Map<String, String> getParams() {
        return param;
    }
}
