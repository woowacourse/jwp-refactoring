package kitchenpos.menu.application.mapper;

import kitchenpos.menu.application.dto.response.MenuProductResponse;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.domain.Menu;

import java.util.stream.Collectors;

public class MenuMapper {

    public static MenuResponse mapToMenuResponse(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getValue(), menu.getMenuGroup().getId(),
                menu.getMenuProducts()
                        .getItems()
                        .stream()
                        .map(menuProduct -> new MenuProductResponse(menuProduct.getSeq(), menu.getId(),
                                menuProduct.getProduct().getId(), menuProduct.getQuantity()))
                        .collect(Collectors.toList()));
    }
}
