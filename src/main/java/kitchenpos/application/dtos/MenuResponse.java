package kitchenpos.application.dtos;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import lombok.Getter;

@Getter
public class MenuResponse {
    private final Long id;
    private final String name;
    private final Long price;
    private final Long MenuGroupId;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(Menu menu, List<MenuProduct> menuProducts) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice().longValue();
        this.MenuGroupId = menu.getMenuGroup().getId();
        this.menuProducts = menuProducts.stream()
                .map(MenuProductResponse::new)
                .collect(Collectors.toList());
    }
}
