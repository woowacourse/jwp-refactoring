package kitchenpos.dto.mapper;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.dto.response.MenuResponse;

public interface MenuDtoMapper {

    MenuResponse toMenuResponse(Menu menu);

    List<MenuResponse> toMenuResponses(List<Menu> menus);
}
