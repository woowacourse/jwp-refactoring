package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductDto;

public class DtoFixture {

    public static MenuCreateRequest getMenuCreateRequest(final Long menuGroupId, final List<MenuProductDto> menuProducts) {
        return new MenuCreateRequest("마이쮸 포도맛", BigDecimal.valueOf(800), menuGroupId, menuProducts);
    }
}
