package kitchenpos.menu.presentation.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.presentation.dto.response.ProductResponse;

public class MenuProductResponse {

    private final Long id;

    private final long quantity;

    private final ProductResponse product;

    private MenuProductResponse(final Long id,
                                final long quantity,
                                final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getId(),
                                       menuProduct.getQuantity(),
                                       ProductResponse.from(menuProduct.getProduct()));
    }

    public static List<MenuProductResponse> convertToList(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                           .map(MenuProductResponse::from)
                           .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public long getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
