package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.domain.Menu;

public class MenuFixture {
    public static Menu createMenu(Long id, String name, Long price, Long menuGroupId) {
        return new Menu(id, name, price != null ? BigDecimal.valueOf(price) : null, menuGroupId);
    }

    public static MenuCreateRequest createMenuRequest(
        String name,
        Long price,
        Long menuGroupId,
        List<MenuProductCreateRequest> menuProducts
    ) {
        return new MenuCreateRequest(
            name,
            price != null ? BigDecimal.valueOf(price) : null,
            menuGroupId,
            menuProducts
        );
    }
}
