package kitchenpos.menu.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductResponse;

public class MenuProductMapper {

    private MenuProductMapper() {
    }

    public static MenuProductResponse toMenuProductResponse(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getProductId(),
                menuProduct.getQuantity().getValue()
        );
    }

    public static List<MenuProductResponse> toMenuProductResponses(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductMapper::toMenuProductResponse)
                .collect(Collectors.toList());
    }
}
