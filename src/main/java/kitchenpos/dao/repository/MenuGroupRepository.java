package kitchenpos.dao.repository;

import java.util.List;
import kitchenpos.dao.jdbctemplate.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepository implements MenuGroupDao {

    private final JdbcTemplateMenuGroupDao menuGroupDao;

    public MenuGroupRepository(JdbcTemplateMenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Override
    public MenuGroup save(MenuGroup entity) {
        return menuGroupDao.save(entity);
    }

    @Override
    public MenuGroup findById(Long id) {
        return menuGroupDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("메뉴 그룹은 DB에 등록되어야 한다."));
    }

    @Override
    public List<MenuGroup> findAll() {
        return menuGroupDao.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return menuGroupDao.existsById(id);
    }
}
