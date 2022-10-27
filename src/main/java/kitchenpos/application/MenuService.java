package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.request.MenuRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

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
        validateMenuGroupExists(request.getMenuGroupId());
        Menu menu = menuDao.save(request.toEntity());
        menu.setIdToMenuProducts();
        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        List<Product> products = findProducts(menuProducts);

        menu.validatePriceUnderProductsSum(products);

        for (final MenuProduct menuProduct : menuProducts) {
            menuProductDao.save(menuProduct);
        }

        return menu;
    }

    private void validateMenuGroupExists(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Product> findProducts(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new))
            .collect(Collectors.toUnmodifiableList());
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
