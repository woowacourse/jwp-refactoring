package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuProductResponse {

    private Long seq;
    private Long productId;
    private long quantity;

    public static List<MenuProductResponse> listFrom(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList());
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        ProductResponse product = ProductResponse.from(menuProduct.getProduct());
        return MenuProductResponse.builder()
            .seq(menuProduct.getSeq())
            .productId(product.getId())
            .quantity(menuProduct.getQuantity())
            .build();
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
