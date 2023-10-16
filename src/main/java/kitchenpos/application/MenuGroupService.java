package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.vo.menugroup.MenuGroupRequest;
import kitchenpos.vo.menugroup.MenuGroupResponse;
import kitchenpos.vo.menugroup.MenuGroupsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = new MenuGroup.Builder()
                .name(menuGroupRequest.getName())
                .build();

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        return MenuGroupResponse.of(savedMenuGroup);
    }

    public MenuGroupsResponse list() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        return MenuGroupsResponse.of(menuGroups);
    }
}
