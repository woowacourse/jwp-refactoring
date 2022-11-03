package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuGroupsResponse;
import kitchenpos.dao.MenuGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = new MenuGroup(request.getName());
        menuGroupDao.save(menuGroup);
        return MenuGroupResponse.from(menuGroup);
    }

    public MenuGroupsResponse list() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return MenuGroupsResponse.from(menuGroups);
    }
}
