package kitchenpos.domain.repository;

import kitchenpos.dao.MenuGroupDao;
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
}
