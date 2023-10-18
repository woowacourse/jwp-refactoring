package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public Menu create(final Menu menu) {

        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        return menuDao.save(menu);
    }

    @Transactional
    public List<Menu> list() {

        return menuDao.findAll();
    }
}
