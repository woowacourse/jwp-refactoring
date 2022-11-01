package kitchenpos.application;

import kitchenpos.dao.menugroup.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
    public MenuGroup create(final MenuGroup menuGroupRequest) {
        return menuGroupDao.save(menuGroupRequest);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
