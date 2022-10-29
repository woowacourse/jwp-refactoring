package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(final MenuDao menuDao, final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(final Menu menu) {
        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = saveMenuProducts(savedMenu.getId(), menu.getMenuProducts());
        savedMenu.setMenuProducts(savedMenuProducts);
        return savedMenu;
    }

    public Optional<Menu> findById(final Long id) {
        return menuDao.findById(id);
    }

    public List<Menu> findAll() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }
        return menus;
    }

    public long countByIdIn(final List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }

    private List<MenuProduct> saveMenuProducts(final Long menuId, final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> menuProductDao.save(
                new MenuProduct(
                    menuId, menuProduct.getProductId(), menuProduct.getQuantity(), menuProduct.getPrice()
                )
            ))
            .collect(Collectors.toUnmodifiableList());
    }
}
