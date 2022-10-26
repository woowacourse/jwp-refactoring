package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;

public interface MenuGroupService {
    MenuGroup create(MenuGroupCreateRequest request);

    List<MenuGroup> list();
}
