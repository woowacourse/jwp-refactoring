package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
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
    public MenuGroup create(final MenuGroupRequest menuGroupRequest) {
        final String name = menuGroupRequest.getName();
        final MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
