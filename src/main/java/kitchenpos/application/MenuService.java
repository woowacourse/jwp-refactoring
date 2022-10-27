package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductRepository menuProductRepository;
    private final ProductDao productDao;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupDao menuGroupDao,
            final MenuProductRepository menuProductRepository,
            final ProductDao productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.menuProductRepository = menuProductRepository;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new NotFoundMenuGroupException();
        }

        validateMenuPrice(menu);
        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = saveMenuProducts(menu.getMenuProducts(), savedMenu);

        savedMenu.updateMenuProducts(savedMenuProducts);
        return savedMenu;
    }

    private void validateMenuPrice(final Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(NotFoundProductException::new);
            sum = sum.add(product.calculateTotalPrice(menuProduct.getQuantity()));
        }
        menu.validatePrice(sum);
    }

    private List<MenuProduct> saveMenuProducts(final List<MenuProduct> menuProducts, final Menu menu) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenu(menu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        return savedMenuProducts;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
