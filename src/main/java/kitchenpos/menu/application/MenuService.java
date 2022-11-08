package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        validateMenuGroupId(request.getMenuGroupId());
        final Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                toManuProduct(request)
        );
        return MenuResponse.of(menuDao.save(menu), menu.getMenuProducts());
    }

    private List<MenuProduct> toManuProduct(final MenuCreateRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(menuProduct -> {
                    Product product = productDao.findById(menuProduct.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new MenuProduct(product.getId(), menuProduct.getQuantity(), product.getPrice());
                }).collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return menuDao.findAll()
                .stream()
                .map(it -> MenuResponse.of(it, menuProductDao.findAllByMenuId(it.getId())))
                .collect(Collectors.toList());
    }

    private void validateMenuGroupId(final Long id) {
        if (!menuGroupDao.existsById(id)) {
            throw new IllegalArgumentException();
        }
    }
}
