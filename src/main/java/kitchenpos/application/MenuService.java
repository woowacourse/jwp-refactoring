package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuCreateRequest request) {
        validateExistMenuGroupId(request.getMenuGroupId());
        validatePrice(request.getPrice());
        validateTotalPrice(request.getPrice(), request.getMenuProducts());

        final Menu menu = menuDao.save(request.toEntity());
        final List<MenuProduct> menuProducts = saveMenuProducts(
            request.getMenuProducts(), menu.getId());

        return MenuResponse.of(menu, menuProducts);
    }

    public List<MenuResponse> list() {
        return menuDao.findAll()
            .stream()
            .map(it -> MenuResponse.of(it, menuProductDao.findAllByMenuId(it.getId())))
            .collect(Collectors.toList());
    }

    private List<MenuProduct> saveMenuProducts(final List<MenuProduct> menuProducts,
        final Long menuId) {
        return menuProducts.stream()
            .map(it -> new MenuProduct(null, menuId, it.getProductId(), it.getQuantity()))
            .map(menuProductDao::save)
            .collect(Collectors.toList());
    }

    private void validateExistMenuGroupId(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    // FIXME 코드 중복
    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTotalPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice()
                .multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
