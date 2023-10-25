package kitchenpos.application;

import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuCreateRequest.MenuProductRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        final String menuName = request.getName();
        final Price menuPrice = new Price(request.getPrice());
        final MenuGroup menuGroup = menuGroupDao.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final Menu.MenuFactory menuFactory = new Menu.MenuFactory(menuName, menuPrice, menuGroup);

        final List<MenuProductRequest> menuProducts = request.getMenuProducts();
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuFactory.addProduct(product, menuProductRequest.getQuantity());
        }

        final Menu menu = menuFactory.create();
        return MenuResponse.from(menuDao.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
