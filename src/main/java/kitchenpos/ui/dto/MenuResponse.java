package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuResponse {
    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    public static MenuResponse from(final Menu menu) {
        final List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
            menuProductResponses);
    }
}
