package kitchenpos.dto.mapper;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.dto.response.MenuCreateResponse;
import kitchenpos.dto.response.MenuResponse;

public interface MenuDtoMapper {

    MenuCreateResponse toMenuCreateResponse(Menu menu);

    List<MenuResponse> toMenuResponses(List<Menu> menus);
}
