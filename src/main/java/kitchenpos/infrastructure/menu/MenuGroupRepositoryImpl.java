package kitchenpos.infrastructure.menu;

import java.util.List;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupRepositoryImpl(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Override
    public MenuGroup get(final Long id) {
        return menuGroupDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public MenuGroup add(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    @Override
    public List<MenuGroup> getAll() {
        return menuGroupDao.findAll();
    }
}
