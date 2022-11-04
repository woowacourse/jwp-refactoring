package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.repository.jdbc.JdbcTemplateMenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

    private final JdbcTemplateMenuGroupDao menuGroupDao;

    public MenuGroupRepositoryImpl(JdbcTemplateMenuGroupDao menuGroupDao) {
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
