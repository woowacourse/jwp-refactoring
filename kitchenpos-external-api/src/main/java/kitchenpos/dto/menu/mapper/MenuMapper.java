package kitchenpos.dto.menu.mapper;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.request.MenuCreateRequest;

public interface MenuMapper {

    Menu toMenu(MenuCreateRequest menuCreateRequest, List<MenuProduct> menuProducts);
}
