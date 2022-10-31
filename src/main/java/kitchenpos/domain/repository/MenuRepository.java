package kitchenpos.domain.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Component;

@Component
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuGroupDao menuGroupDao;

    public MenuRepository(MenuDao menuDao, MenuProductDao menuProductDao, MenuGroupDao menuGroupDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuGroupDao = menuGroupDao;
    }

    public Menu save(Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        Long menuId = menuDao.save(menu).getId();
        List<MenuProduct> savedMenuProducts = saveMenuProducts(menu, menuId);

        return new Menu(menuId, menu.getName(), menu.getPrice(), menu.getMenuGroupId(), savedMenuProducts);
    }

    private List<MenuProduct> saveMenuProducts(Menu menu, Long menuId) {
        return menu.getMenuProducts()
                .stream()
                .map(menuProduct -> new MenuProduct(menuId, menuProduct.getProductId(), menuProduct.getQuantity()))
                .map(menuProductDao::save)
                .collect(Collectors.toList());
    }

    public List<Menu> findAll() {
        return menuDao.findAll()
                .stream()
                .map(menu -> new Menu(
                        menu.getId(),
                        menu.getName(),
                        menu.getPrice(),
                        menu.getMenuGroupId(),
                        menuProductDao.findAllByMenuId(menu.getId())
                )).collect(Collectors.toList());
    }

    public Long countByIdIn(List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
