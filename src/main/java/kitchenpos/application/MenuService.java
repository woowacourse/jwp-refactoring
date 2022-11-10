package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;

    public MenuService(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Transactional
    public Menu create(String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(name, new Price(price), menuGroup.getId(), menuProducts);
        return menuDao.save(menu);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }

    public Menu search(Long menuId) {
        return menuDao.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
