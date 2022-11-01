package kitchenpos.domain.menu;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.menu.Menu;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(final MenuDao menuDao,
                          final MenuGroupDao menuGroupDao,
                          final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(final Menu menu) {
        final Menu savedMenu = menuDao.save(menu);
        menuProductDao.saveAll(menu.getMenuProducts());
        return savedMenu;
    }

    public List<Menu> findAll() {
        return menuDao.findAll();
    }

    public boolean isGroupExist(final Menu menu) {
        return menuGroupDao.existsById(menu.getMenuGroupId());
    }
}
