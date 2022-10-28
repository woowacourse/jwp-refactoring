package kitchenpos.repository;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepository {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupRepository(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroup get(final Long id) {
        return menuGroupDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public MenuGroup add(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> getAll() {
        return menuGroupDao.findAll();
    }
}
