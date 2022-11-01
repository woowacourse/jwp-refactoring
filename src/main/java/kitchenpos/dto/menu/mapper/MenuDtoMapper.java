package kitchenpos.dto.menu.mapper;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.dto.menu.response.MenuResponse;

public interface MenuDtoMapper {

    MenuResponse toMenuResponse(Menu menu);

    List<MenuResponse> toMenuResponses(List<Menu> menus);
}
