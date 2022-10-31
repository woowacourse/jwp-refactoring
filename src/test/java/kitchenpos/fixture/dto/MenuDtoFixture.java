package kitchenpos.fixture.dto;

import static kitchenpos.fixture.domain.MenuFixture.createMenu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.ui.dto.response.MenuCreateResponse;

public class MenuDtoFixture {

    public static MenuCreateRequest createMenuCreateRequest(final String name, final Long price, final Long menuGroupId,
                                                            final List<MenuProductCreateRequest> menuProductsRequest) {
        return new MenuCreateRequest(name, BigDecimal.valueOf(price), menuGroupId, menuProductsRequest);
    }

    public static MenuCreateResponse generateMenuCreateResponse() {
        return MenuCreateResponse.from(createMenu());
    }
}
