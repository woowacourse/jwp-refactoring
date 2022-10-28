package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        validateCreateMenu(request);
        final Menu savedMenu = menuDao.save(request.toEntity());
        final List<MenuProduct> savedMenuProducts = getMenuProducts(request.getMenuProducts(), savedMenu.getId());

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    public List<MenuResponse> list() {
        return menuDao.findAll()
                .stream()
                .map(it -> MenuResponse.of(it, menuProductDao.findAllByMenuId(it.getId())))
                .collect(Collectors.toList());
    }

    private void validateCreateMenu(final MenuCreateRequest request) {
        validateMenuGroupId(request.getMenuGroupId());
        validatePrice(request.getPrice());
        validateTotalPrice(request.getPrice(), request.getMenuProducts());
    }

    private void validateMenuGroupId(final Long id) {
        if (!menuGroupDao.existsById(id)) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePrice(final BigDecimal price) {
        new Price(price);
    }

    private void validateTotalPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())

                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(
                    product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> getMenuProducts(final List<MenuProduct> menuProducts, final Long menuId) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        return savedMenuProducts;
    }
}
