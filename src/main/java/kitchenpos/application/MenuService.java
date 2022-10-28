package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
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
        Menu menu = Menu.from(menuRequest);

        final List<MenuProduct> menuProductResponses = menuRequest.getMenuProducts().stream()
                .map(MenuProduct::from)
                .collect(Collectors.toList());

        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProductResponses) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().getValue().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menu.getPrice().getValue().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(menu);

        List<MenuProduct> menuProducts = menuProductResponses.stream()
                .map(menuProduct -> menuProductDao.save(
                        new MenuProduct(savedMenu.getId(), menuProduct.getProductId(), menuProduct.getQuantity())))
                .collect(Collectors.toList());

        return MenuResponse.of(savedMenu, menuProducts);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu -> MenuResponse.of(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
