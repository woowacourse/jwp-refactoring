package kitchenpos.menu_group.application;

import java.util.List;
import kitchenpos.menu_group.repository.MenuGroupDao;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.dto.MenuGroupRequest;
import kitchenpos.menu_group.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = MenuGroup.builder()
            .name(request.getName())
            .build();
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        return MenuGroupResponse.from(savedMenuGroup);
    }

    @Transactional
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return MenuGroupResponse.listFrom(menuGroups);
    }
}
