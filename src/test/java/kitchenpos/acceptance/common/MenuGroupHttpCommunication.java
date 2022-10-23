package kitchenpos.acceptance.common;

import java.util.Map;

public class MenuGroupHttpCommunication {

    public static HttpCommunication create(final Map<String, Object> requestBody) {
        return HttpCommunication.request()
                .create("/api/menu-groups", requestBody)
                .build();
    }

    public static HttpCommunication getMenuGroups() {
        return HttpCommunication.request()
                .get("/api/menu-groups")
                .build();
    }
}
