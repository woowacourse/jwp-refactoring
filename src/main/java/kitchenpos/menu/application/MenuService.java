package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.response.MenuProductResponse;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
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
        final Menu menu = menuDao.save(request.toEntity());

        validateTotalPrice(request.getPrice(), request.getMenuProducts());
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

    private List<MenuProduct> saveMenuProducts(
        final List<MenuProduct> menuProducts,
        final Long menuId
    ) {
        return menuProducts.stream()
            .map(it -> menuProductDao.save(new MenuProduct(
                menuId,
                it.getProductId(),
                it.getQuantity()
            )))
            .map(menuProductDao::save)
            .collect(Collectors.toList());
    }

    private void validateExistMenuGroupId(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
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
