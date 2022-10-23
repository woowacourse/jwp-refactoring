package kitchenpos.acceptance.common;

import java.util.Map;

public class OrderHttpCommunication {

    public static HttpCommunication create(final Map<String, Object> requestBody) {
        return HttpCommunication.request()
                .create("/api/orders", requestBody)
                .build();
    }

    public static HttpCommunication getOrders() {
        return HttpCommunication.request()
                .get("/api/orders")
                .build();
    }

    public static HttpCommunication changeOrderStatus(final Long orderId, final Map<String, Object> requestBody) {
        final String requestUrl = String.format("/api/orders/%s/order-status", orderId);
        return HttpCommunication.request()
                .update(requestUrl, requestBody)
                .build();
    }
}
