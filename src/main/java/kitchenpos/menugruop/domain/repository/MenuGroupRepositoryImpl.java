package kitchenpos.menugruop.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menugruop.dao.MenuGroupDao;
import kitchenpos.menugruop.domain.MenuGroup;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupRepositoryImpl(final MenuGroupDao menuGroupDao) {
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
