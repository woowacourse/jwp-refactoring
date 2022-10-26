package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.request.MenuCreateRequest;

public interface MenuMapper {

    Menu menuCreateRequestToMenu(MenuCreateRequest menuCreateRequest);
}
