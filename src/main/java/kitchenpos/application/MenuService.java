package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductCreateRequest;
import kitchenpos.ui.response.MenuResponse;
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
