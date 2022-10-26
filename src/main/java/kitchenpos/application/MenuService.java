package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.request.MenuCreateRequest;

public interface MenuService {
    Menu create(final MenuCreateRequest request);

    List<Menu> list();
}
