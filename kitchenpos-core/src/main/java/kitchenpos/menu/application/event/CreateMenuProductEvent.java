package kitchenpos.menu.application.event;

import kitchenpos.common.dto.request.MenuProductDto;
import kitchenpos.menu.domain.Menu;

import java.util.List;

public class CreateMenuProductEvent {

    private final List<MenuProductDto> menuProductDtos;
    private final Menu menu;

    public CreateMenuProductEvent(final List<MenuProductDto> menuProducts, final Menu menu) {
        this.menuProductDtos = menuProducts;
        this.menu = menu;
    }

    public List<MenuProductDto> getMenuProductDtos() {
        return menuProductDtos;
    }

    public Menu getMenu() {
        return menu;
    }
}
