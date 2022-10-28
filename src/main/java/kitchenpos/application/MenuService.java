package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        menuGroupDao.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        validateMenuProductsExistence(request.getMenuProducts());

        final Menu menu = request.toEntity();
        final Menu savedMenu = menuDao.save(menu);
        savedMenu.updateMenuIdOfMenuProducts();
        menuProductDao.saveAll(savedMenu.getMenuProducts());
        return savedMenu;
    }

    private void validateMenuProductsExistence(final List<MenuProduct> menuProducts) {
        for (final MenuProduct menuProduct : menuProducts) {
            productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
