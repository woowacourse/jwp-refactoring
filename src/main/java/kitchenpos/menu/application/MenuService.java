package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menu.ui.request.MenuCreateRequest;
import kitchenpos.menu.ui.request.MenuProductCreateRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.product.repository.ProductDao;
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
        final var menuGroup = menuGroupDao.findById(request.getMenuGroupId())
                                          .orElseThrow(EntityNotFoundException::new);

        final var menuProducts = request.getMenuProducts().stream()
                                        .map(this::crateMenuProductByRequest)
                                        .collect(Collectors.toList());

        final var menu = new Menu(request.getName(), request.getPrice(), menuProducts, menuGroup);
        return MenuResponse.from(menuDao.save(menu));
    }

    private MenuProduct crateMenuProductByRequest(final MenuProductCreateRequest menuProductCreateRequest) {
        final var product = productDao.findById(menuProductCreateRequest.getProductId())
                                      .orElseThrow(EntityNotFoundException::new);
        return new MenuProduct(product, menuProductCreateRequest.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuDao.findAll().stream()
                      .map(MenuResponse::from)
                      .collect(Collectors.toList());
    }
}
