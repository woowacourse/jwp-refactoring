package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
abstract class ServiceTest {

    protected SoftAssertions softly;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @BeforeEach
    void setUp() {
        softly = new SoftAssertions();
    }

    protected Product saveProduct(final String name) {
        return saveProduct(name, BigDecimal.ONE);
    }

    protected Product saveProduct(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return productDao.save(product);
    }

    protected MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroupDao.save(menuGroup);
    }

    @SafeVarargs
    protected final List<MenuProduct> getMenuProducts(final Pair<Product, Long>... pairs) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final Pair<Product, Long> pair : pairs) {
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(pair.getFirst().getId());
            menuProduct.setQuantity(pair.getSecond());
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    @SafeVarargs
    protected final Menu saveMenu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                                  final Pair<Product, Long>... menuProductPairs) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroup.getId());

        final List<MenuProduct> menuProducts = getMenuProducts(menuProductPairs);
        menu.setMenuProducts(menuProducts);

        return menuDao.save(menu);
    }
}
