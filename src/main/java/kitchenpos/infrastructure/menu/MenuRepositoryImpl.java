package kitchenpos.infrastructure.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuDao;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductDao;
import kitchenpos.domain.menu.MenuRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepositoryImpl(final MenuDao menuDao,
                              final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    @Override
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

    @Override
    public List<Menu> getAll() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
