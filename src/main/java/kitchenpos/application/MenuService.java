package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.domain.product.Price;
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
    public MenuResponse create(final String name, final Price price, final Long menuGroupId, final List<MenuProductRequest> menuProductRequests) {
        validateMenuGroupIdExists(menuGroupId);

        final MenuProducts menuProducts = new MenuProducts(menuProductRequests
                .stream().map(this::getMenuProduct)
                .collect(Collectors.toList()));

        if (menuProducts.isBiggerThanTotalPrice(price)) {
            throw new IllegalArgumentException();
        }

        return MenuResponse.from(menuDao.save(new Menu(null, name, price, menuGroupId, menuProducts)));
    }

    private void validateMenuGroupIdExists(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private MenuProduct getMenuProduct(final MenuProductRequest menuProductRequest) {
        final Product product = productDao.findMandatoryById(menuProductRequest.getProductId());
        return new MenuProduct(null, product.getPrice(), product.getId(), menuProductRequest.getQuantity());
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuDao.findAll());
    }
}
