package kitchenpos.dto.order.response;

import kitchenpos.domain.order.OrderedMenuProduct;
import kitchenpos.dto.product.response.ProductResponse;

public class OrderedMenuProductResponse {
    private final Long seq;
    private final ProductResponse response;
    private final long quantity;

    public OrderedMenuProductResponse(final Long seq, final ProductResponse response, final long quantity) {
        this.seq = seq;
        this.response = response;
        this.quantity = quantity;
    }

    public static OrderedMenuProductResponse from(OrderedMenuProduct orderedMenuProduct) {
        return new OrderedMenuProductResponse(
                orderedMenuProduct.seq(),
                ProductResponse.of(orderedMenuProduct.name(), orderedMenuProduct.price().price()),
                orderedMenuProduct.quantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getResponse() {
        return response;
    }

    public long getQuantity() {
        return quantity;
    }
}
