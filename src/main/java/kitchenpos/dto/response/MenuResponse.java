package kitchenpos.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;

public class MenuResponse {

    private Long id;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(Menu menu) {
        id = menu.getId();
        price = menu.getPrice();
        menuGroup = new MenuGroupResponse(menu.getMenuGroup());
        menuProducts = menu.getMenuProducts().stream()
            .map(it -> new MenuProductResponse(it))
            .collect(Collectors.toList());
    }
}
