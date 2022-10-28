package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuProductRequest.Create;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    public MenuResponse create(final MenuRequest request) {
        validateMenuGroupExists(request);
        Menu menu = request.toMenu();

        List<Create> menuProducts = request.getMenuProducts();
        validatePrice(menuProducts, menu);

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        List<MenuProduct> savedMenuProducts = menuProducts.stream()
                .map(it -> menuProductDao.save(new MenuProduct(menuId, it.getProductId(), it.getQuantity())))
                .collect(Collectors.toList());
        savedMenu.setMenuProducts(savedMenuProducts);

        return new MenuResponse(savedMenu);
    }

    private void validateMenuGroupExists(MenuRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("[ERROR] 올바른 메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validatePrice(List<MenuProductRequest.Create> requests, Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest.Create menuProduct : requests) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.multiplyPrice(menuProduct.getQuantity()));
        }
        if (menu.isBiggerPrice(sum)) {
            throw new IllegalArgumentException("[ERROR] 메뉴의 가격이 상품들의 전체 가격보다 크면 안됩니다.");
        }
    }

    public List<MenuResponse> list() {
        return menuDao.findAll().stream()
                .peek(menu -> menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId())))
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }
}
