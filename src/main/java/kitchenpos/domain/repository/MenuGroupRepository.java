package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupRepository {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupRepository(MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public boolean existsById(Long id) {
        return menuGroupDao.existsById(id);
    }

    public MenuGroup save(MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> findAll() {
        return menuGroupDao.findAll();
    }
}
