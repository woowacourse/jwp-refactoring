package kitchenpos.ui.dto;

import kitchenpos.domain.menu.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {

    private Long seq;
    private ProductResponse productResponse;
    private long quantity;

    public MenuProductResponse(Long seq, ProductResponse productResponse, long quantity) {
        this.seq = seq;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> of(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
                ProductResponse.from(menuProduct.getProduct()),
                menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
