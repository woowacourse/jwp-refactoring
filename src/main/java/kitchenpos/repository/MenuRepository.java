package kitchenpos.repository;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;

    public MenuRepository(final MenuDao menuDao,
                          final MenuGroupDao menuGroupDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public Menu save(final Menu entity) {
        if (!menuGroupDao.existsById(entity.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        return menuDao.save(entity);
    }

    public List<Menu> findAll() {
        return menuDao.findAll();
    }
}
