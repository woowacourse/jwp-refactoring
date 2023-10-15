package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
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
    public MenuResponse create(final MenuCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        Long menuGroupId = request.getMenuGroupId();
        if (menuGroupId != null && !menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }

        List<MenuProductCreateRequest> menuProducts = request.getMenuProducts();

        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductCreateRequest menuProductRequest : menuProducts) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(request.toEntity());

        for (final MenuProductCreateRequest menuProductRequest : menuProducts) {
            MenuProduct newMenuProduct = new MenuProduct(
                null,
                savedMenu.getId(),
                menuProductRequest.getProductId(),
                menuProductRequest.getQuantity());
            savedMenu.addMenuProduct(menuProductDao.save(newMenuProduct));
        }

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return MenuResponse.of(menus);
    }
}
