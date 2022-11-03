package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

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
        final List<MenuProduct> savedMenuProducts = saveMenuProducts(menu.getMenuProducts());
        return new Menu(
            savedMenu.getId(),
            savedMenu.getName(),
            savedMenu.getPrice(),
            savedMenu.getMenuGroupId(),
            savedMenuProducts
        );
    }

    public Optional<Menu> findById(final Long id) {
        return menuDao.findById(id);
    }

    public List<Menu> findAll() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
            .map(menu -> new Menu(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProductDao.findAllByMenuId(menu.getId())
            ))
            .collect(Collectors.toUnmodifiableList());
    }

    public long countByIdIn(final List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }

    private List<MenuProduct> saveMenuProducts(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> menuProductDao.save(
                new MenuProduct(
                    menuProduct.getProductId(), menuProduct.getQuantity()
                )
            ))
            .collect(Collectors.toUnmodifiableList());
    }
}
