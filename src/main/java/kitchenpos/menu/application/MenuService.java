package kitchenpos.menu.application;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menu.repository.MenuGroupDao;
import kitchenpos.menu.repository.MenuProductDao;
import kitchenpos.menu.repository.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
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
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 속한 메뉴그룹이 존재하지 않습니다."));

        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, Collections.emptyList());

        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        final List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map((menuProductRequest) -> convertToMenuProduct(menu, menuProductRequest))
                .collect(Collectors.toList());

        menu.changeMenuProducts(menuProducts);
        return MenuResponse.from(menuDao.save(menu));
    }

    private MenuProduct convertToMenuProduct(Menu menu, MenuProductRequest menuProductRequest) {
        final Product product = productDao.findById(menuProductRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴에 등록하려는 상품이 존재하지 않습니다."));
        return new MenuProduct(menu, product, menuProductRequest.getQuantity());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
