package kitchenpos.dto.mapper;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.request.MenuCreateRequest;

public interface MenuMapper {

    Menu toMenu(MenuCreateRequest menuCreateRequest, List<MenuProduct> menuProducts);
}
