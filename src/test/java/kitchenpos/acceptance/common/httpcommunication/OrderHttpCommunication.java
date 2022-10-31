package kitchenpos.acceptance.common.httpcommunication;

import java.util.Map;

public class OrderHttpCommunication {

    public static HttpCommunication create(final Map<String, Object> requestBody) {
        return HttpCommunication.request()
                .create("/api/v2/orders", requestBody)
                .build();
    }

    public static HttpCommunication getOrders() {
        return HttpCommunication.request()
                .get("/api/v2/orders")
                .build();
    }

    public static HttpCommunication changeOrderStatus(final Long orderId, final Map<String, Object> requestBody) {
        final String requestUrl = String.format("/api/v2/orders/%s/order-status", orderId);
        return HttpCommunication.request()
                .update(requestUrl, requestBody)
                .build();
    }
}
