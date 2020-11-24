package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
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
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(request.getName()));

        return MenuGroupResponse.of(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.of(menuGroupDao.findAll());
    }
}
