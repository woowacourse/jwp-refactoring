package kitchenpos.acceptance.common;

import java.util.Map;

public class ProductHttpCommunication {

    public static HttpCommunication create(final Map<String, Object> requestBody) {
        return HttpCommunication.request()
                .create("/api/products", requestBody)
                .build();
    }

    public static HttpCommunication getProducts() {
        return HttpCommunication.request()
                .get("/api/products")
                .build();
    }
}
