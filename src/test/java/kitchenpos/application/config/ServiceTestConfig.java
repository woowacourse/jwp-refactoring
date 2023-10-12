package kitchenpos.application.config;

import kitchenpos.common.DataTestExecutionListener;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

import java.math.BigDecimal;
import java.util.ArrayList;

@JdbcTest
@Import(DaoConfig.class)
@TestExecutionListeners(value = DataTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ServiceTestConfig {

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    protected Product saveProduct() {
        final Product product = new Product();
        product.setName("여우 상품");
        product.setPrice(BigDecimal.valueOf(10000));
        return productDao.save(product);
    }

    protected MenuGroup saveMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴 그룹");
        return menuGroupDao.save(menuGroup);
    }

    protected Menu saveMenu(final MenuGroup menuGroup) {
        final Menu menu = new Menu();
        menu.setName("메뉴 이름");
        menu.setPrice(BigDecimal.valueOf(2000));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(new ArrayList<>());
        return menuDao.save(menu);
    }

    protected MenuProduct saveMenuProduct(final Product product, final Menu menu) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(2L);
        menuProduct.setProductId(product.getId());
        menuProduct.setMenuId(menu.getId());
        return menuProductDao.save(menuProduct);
    }
}
