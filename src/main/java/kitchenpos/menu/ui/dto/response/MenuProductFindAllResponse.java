package kitchenpos.menu.ui.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProducts;

public class MenuProductFindAllResponse {

    private Long productId;
    private long quantity;

    public MenuProductFindAllResponse() {
    }

    private MenuProductFindAllResponse(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductFindAllResponse> from(final MenuProducts menuProducts) {
        return menuProducts.getMenuProducts()
                .stream()
                .map(menuProduct -> new MenuProductFindAllResponse(menuProduct.getProductId(),
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
