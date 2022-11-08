package kitchenpos.menu.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.infra.JdbcTemplateMenuGroupDao;
import kitchenpos.menu.infra.MenuGroupDao;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepository implements MenuGroupDao {

    private final JdbcTemplateMenuGroupDao menuGroupDao;

    public MenuGroupRepository(final JdbcTemplateMenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Override
    public MenuGroup save(final MenuGroup entity) {
        return menuGroupDao.save(entity);
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return menuGroupDao.findById(id);
    }

    @Override
    public List<MenuGroup> findAll() {
        return menuGroupDao.findAll();
    }

    @Override
    public boolean existsById(final Long id) {
        return menuGroupDao.existsById(id);
    }
}
