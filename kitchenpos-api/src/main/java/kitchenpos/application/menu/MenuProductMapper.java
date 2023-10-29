package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.menu.dto.MenuProductRequest;
import kitchenpos.menu.MenuProduct;

public abstract class MenuProductMapper {

    public static MenuProduct map(final MenuProductRequest request) {
        return new MenuProduct(request.getProductId(), request.getQuantity());
    }

    public static List<MenuProduct> mapToList(final List<MenuProductRequest> requests) {
        return requests.stream()
                .map(MenuProductMapper::map)
                .collect(Collectors.toList());
    }
}
