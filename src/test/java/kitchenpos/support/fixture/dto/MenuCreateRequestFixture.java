package kitchenpos.support.fixture.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductRequest;

public abstract class MenuCreateRequestFixture {

    public static MenuCreateRequest menuCreateRequest(final String name,
                                                      final Long price,
                                                      final Long menuGroupId) {
        return menuCreateRequest(name, price, menuGroupId, null);
    }

    public static MenuCreateRequest menuCreateRequest(final String name,
                                                      final Long price,
                                                      final Long menuGroupId,
                                                      final List<MenuProductRequest> menuProducts) {
        return new MenuCreateRequest(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }
}