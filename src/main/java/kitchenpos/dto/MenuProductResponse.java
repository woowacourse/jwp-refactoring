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
    private ProductResponse product;
    private long quantity;

    public static List<MenuProductResponse> listFrom(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList());
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        ProductResponse product = ProductResponse.from(menuProduct.getProduct());
        return MenuProductResponse.builder()
            .seq(menuProduct.getSeq())
            .product(product)
            .quantity(menuProduct.getQuantity())
            .build();
    }
}
