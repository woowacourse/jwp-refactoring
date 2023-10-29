package kitchenpos.menu.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class MenuProductCreateRequest {

    private final Long productId;
    private final long quantity;

    @JsonCreator
    public MenuProductCreateRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuProductCreateRequest)) return false;
        MenuProductCreateRequest that = (MenuProductCreateRequest) o;
        return quantity == that.quantity && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }
}
