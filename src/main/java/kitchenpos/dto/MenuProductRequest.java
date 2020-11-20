package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;

public class MenuProductRequest {

    private Long productId;
    private Long quantity;

    private MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public static List<MenuProductRequest> toRequestList(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductRequest::of)
            .collect(Collectors.toList());
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(Menu savedMenu, Product product) {
        return new MenuProduct(savedMenu, product, quantity);
    }
}