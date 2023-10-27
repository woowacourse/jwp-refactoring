package application;

import domain.Menu;
import domain.MenuProduct;
import exception.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.MenuDao;
import repository.MenuGroupDao;
import repository.ProductDao;
import ui.request.MenuCreateRequest;
import ui.request.MenuProductCreateRequest;
import ui.response.MenuResponse;

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

        final var menu = new Menu(request.getName(), request.getPrice(), menuProducts, menuGroup.getId());
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
