package kitchenpos.application;

import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Menu create(final MenuRequest menuRequest) {
        validateMenuGroupExist(menuRequest.getMenuGroupId());
        BigDecimal totalPrice = calcaulateTotalPrice(menuRequest);

        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(new Menu(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuRequest.getMenuGroupId()
        ));

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            MenuProduct menuProduct = new MenuProduct(
                    menuId, menuProductRequest.getProductId(), menuProductRequest.getQuantity());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }


        return ;
    }

    private BigDecimal calcaulateTotalPrice(MenuRequest menuRequest) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);

            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }
        return sum;
    }

    private void validateMenuGroupExist(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
