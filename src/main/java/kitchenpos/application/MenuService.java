package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
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
    public MenuResponse create(final Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 집합이 존재하지 않습니다.");
        }

        Menu savedMenu = menuDao.save(
                new Menu(
                        menu.getName(),
                        menu.getPrice(),
                        menu.getMenuGroupId(),
                        findMenuProduct(menu.getMenuProducts()))
        );
        return new MenuResponse(savedMenu);
    }

    private List<MenuProduct> findMenuProduct(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> {
                    Product product = findProduct(menuProduct.getProductId());
                    return new MenuProduct(product.getId(), product.getPrice(), menuProduct.getQuantity());
                }).collect(Collectors.toList());
    }

    private Product findProduct(Long productId) {
        return productDao.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    public List<MenuResponse> list() {
        return menuDao.findAll()
                .stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }
}
