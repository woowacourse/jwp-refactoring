package kitchenpos.menu.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.service.dto.MenuProductRequest;
import kitchenpos.menu.service.dto.MenuRequest;

public class MenuMapper {

    private MenuMapper() {
    }

    public static Menu toMenu(final MenuRequest request) {
        return new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                toMenuProduct(request.getMenuProducts())
        );
    }

    private static List<MenuProduct> toMenuProduct(final List<MenuProductRequest> request) {
        return request.stream()
                .map(each -> new MenuProduct(each.getProductId(), each.getQuantity()))
                .collect(Collectors.toList());
    }
}
