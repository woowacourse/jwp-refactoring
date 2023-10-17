package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
