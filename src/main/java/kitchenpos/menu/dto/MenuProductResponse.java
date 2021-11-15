package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {

    private final Long id;
    private final Long seq;
    private final Long productId;
    private final Long quantity;

    public MenuProductResponse(Long id, Long seq, Long productId, Long quantity) {
        this.id = id;
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getId(), menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public static List<MenuProductResponse> listOf(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
