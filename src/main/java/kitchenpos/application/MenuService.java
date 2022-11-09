package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dao.MenuDao;
import kitchenpos.application.dao.MenuGroupDao;
import kitchenpos.application.dao.MenuProductDao;
import kitchenpos.application.dao.ProductDao;
import kitchenpos.application.request.MenuRequest;
import kitchenpos.application.request.MenuRequest.MenuProductRequest;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductQuantity;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("menuGroup이 존재하지 않습니다.");
        }

        final List<MenuProductRequest> menuProducts = request.getMenuProducts();
        if (request.getPrice()
                .compareTo(getMenuProductTotalPrice(menuProducts)) > 0) {
            throw new IllegalArgumentException("주문 총 금액이 메뉴 가격보다 작습니다.");
        }
        return createMenuResponse(request, menuProducts);
    }

    private MenuResponse createMenuResponse(final MenuRequest request, final List<MenuProductRequest> menuProducts) {
        final Menu menu = menuDao.save(new Menu(request.getName(), request.getPrice(), request.getMenuGroupId()));
        List<MenuProduct> savedMenuProducts = menuProducts.stream()
                .map(it -> new MenuProduct(menu.getId(), it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
        return MenuResponse.from(menu, savedMenuProducts);
    }

    private BigDecimal getMenuProductTotalPrice(final List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(it -> new MenuProductQuantity(getProductById(it.getProductId()), it.getQuantity()))
                .map(MenuProductQuantity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Product getProductById(final Long productId) {
        return productDao.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 product입니다."));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(it -> MenuResponse.from(it, menuProductDao.findAllByMenuId(it.getId())))
                .collect(Collectors.toList());
    }
}
