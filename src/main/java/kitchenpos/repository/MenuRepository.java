package kitchenpos.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.exception.NotFoundMenuGroupException;
import org.springframework.stereotype.Component;

@Component
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(MenuDao menuDao, MenuGroupDao menuGroupDao, MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(Menu menu) {
        return menuDao.save(menu);
    }

    public void validateMenuGroupId(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new NotFoundMenuGroupException();
        }
    }

    public List<Menu> findAll() {
        List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(menu -> new Menu(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
