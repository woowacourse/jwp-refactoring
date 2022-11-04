package kitchenpos.infrastructure.menu;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
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
        final var saved = menuDao.save(menu);
        final var menuId = saved.getId();
        final var menuProducts = menu.getMenuProducts();
        if (isNewEntities(menuProducts)) {
            final var savedMenuProducts = saveAllMenuProducts(menuProducts, menuId);

            return new Menu(
                    saved.getId(),
                    saved.getName(),
                    saved.getPrice(),
                    saved.getMenuGroupId(),
                    savedMenuProducts);
        }
        updateMenuProducts(menuProducts, menuId);
        return get(saved.getId());
    }

    private boolean isNewEntities(final List<MenuProduct> menuProducts) {
        return menuProducts.get(0).isNew();
    }

    private void updateMenuProducts(final List<MenuProduct> menuProducts, final Long menuId) {
        menuProducts.forEach(menuProduct -> {
            final var entity = new MenuProduct(
                    menuProduct.getSeq(),
                    menuId,
                    menuProduct.getProductId(),
                    menuProduct.getQuantity());
            menuProductDao.update(entity);
        });
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

    @Override
    public Menu get(final Long id) {
        final var menu = menuDao.findById(id)
                .orElseThrow(NoResultException::new);
        final var menuProducts = menuProductDao.findAllByMenuId(id);

        return new Menu(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProducts);
    }
}
