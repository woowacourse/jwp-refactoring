package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
        Product product = menuProduct.getProduct();
        return MenuProductResponse.builder()
            .seq(menuProduct.getSeq())
            .productId(product.getId())
            .quantity(menuProduct.getQuantity())
            .build();
    }
}
