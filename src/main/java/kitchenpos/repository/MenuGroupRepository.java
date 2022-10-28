package kitchenpos.repository;

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
}
