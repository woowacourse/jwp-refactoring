package kitchenpos.ui.dto;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.response.MenuCreateResponse;
import kitchenpos.ui.dto.response.MenuResponse;

public interface MenuMapper {

    Menu createRequestToMenu(MenuCreateRequest menuCreateRequest);

    MenuCreateResponse menuToCreateResponse(Menu menu);

    List<MenuResponse> menusToResponses(List<Menu> menus);
}
