package kitchenpos.acceptance.common.httpcommunication;

import java.util.Map;

public class MenuHttpCommunication {

    public static HttpCommunication create(final Map<String, Object> requestBody) {
        return HttpCommunication.request()
                .create("/api/menus", requestBody)
                .build();
    }

    public static HttpCommunication getMenus() {
        return HttpCommunication.request()
                .get("/api/menus")
                .build();
    }
}
