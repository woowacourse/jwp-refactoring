package kitchenpos.acceptance.common.httpcommunication;

import java.util.Map;

public class MenuGroupHttpCommunication {

    public static HttpCommunication create(final Map<String, Object> requestBody) {
        return HttpCommunication.request()
                .create("/api/v2/menu-groups", requestBody)
                .build();
    }

    public static HttpCommunication getMenuGroups() {
        return HttpCommunication.request()
                .get("/api/v2/menu-groups")
                .build();
    }
}
