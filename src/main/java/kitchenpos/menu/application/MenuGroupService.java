package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.application.dto.MenuGroupCreateRequest;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = new MenuGroup(request.getName());
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
