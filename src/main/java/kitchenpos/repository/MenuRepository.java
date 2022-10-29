package kitchenpos.repository;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductSaveRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;
    private final ProductDao productDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(final MenuDao menuDao,
                          final ProductDao productDao,
                          final MenuGroupDao menuGroupDao,
                          final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.productDao = productDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(final Menu entity) {
        if (!menuGroupDao.existsById(entity.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        Menu savedMenu = menuDao.save(entity);
        savedMenu.addMenuProducts(saveMenuProducts(entity, savedMenu));

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
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
