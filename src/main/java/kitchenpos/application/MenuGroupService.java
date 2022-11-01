package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
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
        final MenuGroup menuGroup = request.toEntity();
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        return MenuGroupResponse.from(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return MenuGroupResponse.from(menuGroups);
    }
}
