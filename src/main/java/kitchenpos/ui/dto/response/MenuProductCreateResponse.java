package kitchenpos.ui.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProducts;

public class MenuProductCreateResponse {

    private Long productId;
    private long quantity;

    public MenuProductCreateResponse() {
    }

    private MenuProductCreateResponse(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductCreateResponse> from(final MenuProducts menuProducts) {
        return menuProducts.getMenuProducts()
                .stream()
                .map(menuProduct -> new MenuProductCreateResponse(menuProduct.getProduct().getId(),
                        menuProduct.getQuantity()))
                .collect(Collectors.toList());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
