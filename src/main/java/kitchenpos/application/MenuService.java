package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Products;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
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
    public MenuResponse create(final MenuRequest menuRequest) {
        final Menu menu = menuRequest.toMenu();

        validate(menu);
        menuDao.save(menu);
        saveMenuProducts(menu);
        return MenuResponse.of(menu);
    }

    private void validate(Menu menu) {
        validateMenuGroupId(menu);
        validatePrice(menu);
    }

    private void validateMenuGroupId(Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePrice(Menu menu) {
        final Products products = new Products(productDao.findAllById(menu.getProductIds()));
        menu.validatePrice(products);
    }

    private void saveMenuProducts(Menu menu) {
        List<MenuProduct> menuProducts = menu.getMenuProducts();

        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menu.getId());
        }
        menuProductDao.saveAll(menuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return MenuResponse.ofList(menus);
    }
}
