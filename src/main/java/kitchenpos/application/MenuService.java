package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
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
    public MenuResponse create(final MenuCreateRequest request) {
        validateMenuGroup(request.getMenuGroupId());
        validateMenuPrice(request.getPrice(), request.getMenuProducts());
        final Menu savedMenu = saveMenu(request);
        return MenuResponse.toResponse(savedMenu);
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private void validateMenuPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = findProduct(menuProduct);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("[ERROR] 총 금액이 각 상품의 합보다 큽니다.");
        }
    }

    private Product findProduct(final MenuProduct menuProduct) {
        return productDao.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다."));
    }

    private Menu saveMenu(final MenuCreateRequest request) {
        return menuDao.save(
                new Menu(
                        request.getName(),
                        request.getPrice(),
                        request.getMenuGroupId(),
                        request.getMenuProducts()
                )
        );
    }

    public List<MenuResponse> list() {
        return menuDao.findAll().stream()
                .map(menu -> MenuResponse.toResponse(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
