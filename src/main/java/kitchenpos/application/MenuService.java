package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
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
        final List<Product> products = getProducts(menuRequest);
        final Menu menu = menuRequest.toEntity(products);

        validateMenuGroupExist(menu.getMenuGroupId());
        validateMenuPrice(menu);

        final Menu savedMenu = menuDao.save(menu);

        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProduct.setMenu(savedMenu);
            menuProductDao.save(menuProduct);
        }

        return MenuResponse.of(savedMenu);
    }

    private List<Product> getProducts(MenuRequest menuRequest) {
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();
        final List<Long> productIds = menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
        return productDao.findByIds(productIds);
    }

    private void validateMenuGroupExist(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateMenuPrice(final Menu menu) {
        if (menu.isPriceGreaterThanMenuProductsPrice()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
