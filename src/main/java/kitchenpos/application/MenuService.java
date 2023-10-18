package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.exception.MenuGroupNotFoundException;
import kitchenpos.application.exception.ProductNotFoundException;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.CreateMenuProductRequest;
import kitchenpos.ui.dto.request.CreateMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final CreateMenuRequest request) {
        final MenuGroup menuGroup = menuGroupDao.findById(request.getMenuGroupId())
                                                .orElseThrow(MenuGroupNotFoundException::new);
        final List<MenuProduct> menuProducts = findMenuProducts(request);
        final Menu menu = Menu.of(request.getName(), request.getPrice(), menuProducts, menuGroup);

        return menuDao.save(menu);
    }

    private List<MenuProduct> findMenuProducts(final CreateMenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (final CreateMenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                                              .orElseThrow(ProductNotFoundException::new);

            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }

        return menuProducts;
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
