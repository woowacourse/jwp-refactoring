package kitchenpos.domain.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(final MenuDao menuDao,
                          final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu add(final Menu menu) {
        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final var savedMenuProducts = saveAllMenuProducts(menu.getMenuProducts(), menuId);

        return new Menu(
                savedMenu.getId(),
                savedMenu.getName(),
                savedMenu.getPrice(),
                savedMenu.getMenuGroupId(),
                savedMenuProducts);
    }

    private List<MenuProduct> saveAllMenuProducts(final List<MenuProduct> menuProducts, final Long menuId) {
        return menuProducts.stream()
                .map(menuProduct -> {
                    final var entity = new MenuProduct(
                            menuId,
                            menuProduct.getProduct(),
                            menuProduct.getQuantity());
                    return menuProductDao.save(entity);
                }).collect(Collectors.toList());
    }

    public List<Menu> getAll() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
