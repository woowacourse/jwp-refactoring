package kitchenpos.core.menugroup.application;

import java.util.List;
import kitchenpos.core.menugroup.domain.MenuGroup;
import kitchenpos.core.menugroup.domain.MenuGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final MenuGroup menuGroupRequest) {
        return menuGroupDao.save(menuGroupRequest);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
