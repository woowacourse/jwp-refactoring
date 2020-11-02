package kitchenpos.ui.dto;

import java.util.Objects;
import javax.validation.constraints.NotNull;

public class MenuProductRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Long quantity;

    private MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, long quantity) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProductRequest that = (MenuProductRequest) o;
        return Objects.equals(productId, that.productId) &&
            Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }

    @Override
    public String toString() {
        return "MenuProductRequest{" +
            "productId=" + productId +
            ", quantity=" + quantity +
            '}';
    }
}
