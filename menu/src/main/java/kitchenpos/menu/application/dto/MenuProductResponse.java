package kitchenpos.menu.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    @JsonProperty("seq")
    private Long seq;
    @JsonProperty("product")
    private ProductResponse productResponse;
    @JsonProperty("quantity")
    private long quantity;

    private MenuProductResponse(
            Long seq,
            ProductResponse productResponse,
            long quantity
    ) {
        this.seq = seq;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.seq(),
                ProductResponse.from(menuProduct.product()),
                menuProduct.quantity()
        );
    }

    public Long seq() {
        return seq;
    }

    public ProductResponse productResponse() {
        return productResponse;
    }

    public long quantity() {
        return quantity;
    }
}
