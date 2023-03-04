package ir.welldone.appmanager.view.rest;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public class AppManagerMediaTypes {

    public static final String V1 = "application/vnd.app-manager.api.v1+json;charset=UTF-8";
    public static final MediaType APPLICATION_APP_MANAGER_V1 = new MediaType("application", "vnd.app-manager.api.v1+json", StandardCharsets.UTF_8);

}
