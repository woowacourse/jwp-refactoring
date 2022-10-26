package kitchenpos.acceptance.common.httpcommunication;

import java.util.Map;

public class TableGroupHttpCommunication {

    public static HttpCommunication create(final Map<String, Object> requestBody) {
        return HttpCommunication.request()
                .create("/api/table-groups", requestBody)
                .build();
    }

    public static HttpCommunication ungroup(final Long tableGroupId) {
        final String requestUrl = String.format("/api/table-groups/%s", tableGroupId);
        return HttpCommunication.request()
                .delete(requestUrl)
                .build();
    }
}
