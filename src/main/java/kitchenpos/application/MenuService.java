package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;

    public MenuService(
            final MenuDao menuDao
    ) {
        this.menuDao = menuDao;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final Menu savedMenu = menuDao.save(menu);
        savedMenu.checkValid();
        return savedMenu;
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
