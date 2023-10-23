package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepository implements MenuGroupDao {

    private final JdbcTemplateMenuGroupDao jdbcTemplateMenuProductDao;

    public MenuGroupRepository(final JdbcTemplateMenuGroupDao jdbcTemplateMenuProductDao) {
        this.jdbcTemplateMenuProductDao = jdbcTemplateMenuProductDao;
    }

    @Override
    public MenuGroup save(final MenuGroup entity) {
        return jdbcTemplateMenuProductDao.save(entity);
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return jdbcTemplateMenuProductDao.findById(id);
    }

    @Override
    public List<MenuGroup> findAll() {
        return jdbcTemplateMenuProductDao.findAll();
    }

    @Override
    public boolean existsById(final Long id) {
        return jdbcTemplateMenuProductDao.existsById(id);
    }
}
