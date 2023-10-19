package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {

    private MenuMapper() {
    }

    public static Menu mapToMenu(final MenuCreateRequest menuCreateRequest) {
        final List<MenuProduct> menuProducts = menuCreateRequest.getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new Menu(menuCreateRequest.getName(), menuCreateRequest.getPrice(),
                menuCreateRequest.getMenuGroupId(), menuProducts);
    }
}
