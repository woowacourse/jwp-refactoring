package kitchenpos.menu.application.mapper;

import kitchenpos.menu.application.dto.response.MenuProductResponse;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {

    private MenuMapper() {
    }
    
    public static MenuResponse mapToResponse(final Menu menu) {
        final List<MenuProductResponse> menuProductResponses = menu.getMenuProducts()
                .getValues()
                .stream()
                .map(it -> new MenuProductResponse(it.getSeq(), it.getProduct().getId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getValue(), menu.getMenuGroup().getId(), menuProductResponses);
    }
}
