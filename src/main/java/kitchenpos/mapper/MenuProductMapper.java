package kitchenpos.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.menu.MenuProductResponse;

public class MenuProductMapper {

    private MenuProductMapper() {
    }

    public static MenuProductResponse toMenuProductResponse(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }

    public static List<MenuProductResponse> toMenuProductResponses(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> new MenuProductResponse(
                                menuProduct.getSeq(),
                                menuProduct.getMenuId(),
                                menuProduct.getProductId(),
                                menuProduct.getQuantity()
                        )
                ).collect(Collectors.toList());
    }
}
