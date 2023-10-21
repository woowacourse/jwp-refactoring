package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;
import kitchenpos.dto.MenuResponse;
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
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        if (!menuGroupDao.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        Menu menu = Menu.of(menuCreateRequest.getName(), menuCreateRequest.getPrice(),
                menuCreateRequest.getMenuGroupId());

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductCreateRequest menuProduct : menuCreateRequest.getMenuProducts()) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        menu.validateMenuProductTotalPrice(sum);

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductCreateRequest menuPro : menuCreateRequest.getMenuProducts()) {
            MenuProduct menuProduct = new MenuProduct(menuId, menuPro.getProductId(), menuPro.getQuantity());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
