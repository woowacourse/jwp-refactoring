package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(new Menu(request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                mapToMenuProduct(request.getMenuProducts())));

        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> mapToMenuProduct(final List<MenuProductCreateRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(it -> {
                    final Product product = productDao.findById(it.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new MenuProduct(it.getMenuId(), product.getId(), it.getQuantity(), product.getPrice());
                })
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return menuDao.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
