package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
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

    public Menu create(final MenuRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        final Menu savedMenu = menuDao.save(new Menu(request.getName(), request.getPrice(), request.getMenuGroupId()));

        final Long menuId = savedMenu.getId();
        Long sequence = 1L;
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final MenuProduct menuProduct = new MenuProduct(sequence++, menuId, menuProductRequest.getProductId(), menuProductRequest.getQuantity());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.updateMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.updateMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
