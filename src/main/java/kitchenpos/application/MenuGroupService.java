package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.ui.dto.MenuGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroups;

    public MenuGroupService(final MenuGroupRepository menuGroups) {
        this.menuGroups = menuGroups;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest request) {
        final var menuGroup = request.toEntity();
        return menuGroups.add(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroups.getAll();
    }
}
