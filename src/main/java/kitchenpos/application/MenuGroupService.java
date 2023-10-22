package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupNameRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupNameRequest menuGroupNameRequest) {
        final String name = menuGroupNameRequest.getName();
        final MenuGroup menuGroup = MenuGroup.from(name);
        final MenuGroup saveMenuGroup = menuGroupDao.save(menuGroup);
        return MenuGroupResponse.of(saveMenuGroup.getId(), saveMenuGroup.getName());
    }

    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return menuGroups.stream()
                .map(menuGroup -> MenuGroupResponse.of(menuGroup.getId(), menuGroup.getName()))
                .collect(Collectors.toList());
    }
}
