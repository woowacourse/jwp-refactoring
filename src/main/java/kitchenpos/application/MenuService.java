package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.dao.MenuCustomDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.product.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuCustomDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuCustomDao menuDao,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final String name, final Price price, final Long menuGroupId, final List<MenuProductRequest> menuProductRequests) {
        validateMenuGroupId(menuGroupId);

        final List<MenuProduct> menuProducts = menuProductRequests
                .stream().map(MenuProductRequest::toEntity)
                .collect(Collectors.toList());
        validatePrice(price, menuProductRequests);

        return MenuResponse.from(menuDao.save(new Menu(null, name, price, menuGroupId, menuProducts)));
    }

    private void validateMenuGroupId(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePrice(final Price price, final List<MenuProductRequest> menuProductRequests) {
        if (price == null) {
            throw new IllegalArgumentException();
        }

        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProductRequest productRequest : menuProductRequests) {
            final Product product = productDao.findMandatoryById(productRequest.getProductId());
            sum = sum.add(product.multiplyPrice(new Price(BigDecimal.valueOf(productRequest.getQuantity()))));
        }

        if (price.isBiggerThan(sum)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuDao.findAll());
    }
}
