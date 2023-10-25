package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.application.dto.ProductResponse;

public class MenuProductResponse {

    private long id;
    private long productId;
    private long quantity;

    private MenuProductResponse(final long id, final long productId, final long quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
