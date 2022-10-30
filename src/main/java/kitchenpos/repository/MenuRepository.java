package kitchenpos.repository;

import static java.util.stream.Collectors.*;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(final MenuDao menuDao,
                          final MenuGroupDao menuGroupDao,
                          final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
    }

    @Transactional
    public Menu save(final Menu entity) {
        if (!menuGroupDao.existsById(entity.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        Menu savedMenu = menuDao.save(entity);
        savedMenu.changeMenuProducts(saveMenuProducts(entity, savedMenu));

        return savedMenu;
    }

    private List<MenuProduct> saveMenuProducts(final Menu entity, final Menu savedMenu) {
        return entity.getMenuProducts()
                .stream()
                .map(it -> toMenuProduct(savedMenu, it))
                .map(menuProductDao::save)
                .collect(toList());
    }

    private MenuProduct toMenuProduct(final Menu savedMenu, final MenuProduct menuProduct) {
        return new MenuProduct(
                savedMenu.getId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity(),
                menuProduct.getPrice()
        );
    }

    public List<Menu> findAll() {
        List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
