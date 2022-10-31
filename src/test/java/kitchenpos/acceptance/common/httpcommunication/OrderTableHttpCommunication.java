package kitchenpos.acceptance.common.httpcommunication;

import java.util.Map;

public class OrderTableHttpCommunication {

    public static HttpCommunication create(final Map<String, Object> requestBody) {
        return HttpCommunication.request()
                .create("/api/v2/tables", requestBody)
                .build();
    }

    public static HttpCommunication getOrderTables() {
        return HttpCommunication.request()
                .get("/api/v2/tables")
                .build();
    }

    public static HttpCommunication changeEmpty(final Long orderTableId, final Map<String, Object> requestBody) {
        final String requestUrl = String.format("/api/v2/tables/%s/empty", orderTableId);
        return HttpCommunication.request()
                .update(requestUrl, requestBody)
                .build();
    }

    public static HttpCommunication changeNumberOfGuests(final Long orderTableId,
                                                         final Map<String, Object> requestBody) {
        final String requestUrl = String.format("/api/v2/tables/%s/number-of-guests", orderTableId);
        return HttpCommunication.request()
                .update(requestUrl, requestBody)
                .build();
    }
}
