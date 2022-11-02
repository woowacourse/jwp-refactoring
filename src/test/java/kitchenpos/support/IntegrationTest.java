package kitchenpos.support;

import static kitchenpos.support.fixture.MenuFixture.createPepperoniMenu;
import static kitchenpos.support.fixture.MenuGroupFixture.createSaleMenuGroup;
import static kitchenpos.support.fixture.MenuProductFixture.createMenuProduct;
import static kitchenpos.support.fixture.ProductFixture.createPepperoni;
import static kitchenpos.support.fixture.ProductFixture.createPineapple;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menu.repository.MenuGroupDao;
import kitchenpos.menu.repository.MenuProductDao;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.table.repository.OrderTableDao;
import kitchenpos.menu.repository.ProductDao;
import kitchenpos.table.repository.TableGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class IntegrationTest {

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    protected MenuGroup menuGroup;
    protected Menu menu;
    protected Product product1;
    protected Product product2;
    protected List<MenuProduct> menuProducts = new ArrayList<>();

    @BeforeEach
    void setup() {
        menuGroup = menuGroupDao.save(createSaleMenuGroup());
        menu = menuDao.save(createPepperoniMenu(menuGroup, menuProducts));
        product1 = productDao.save(createPepperoni());
        product2 = productDao.save(createPineapple());
        menuProducts.add(menuProductDao.save(createMenuProduct(menu, product1)));
        menuProducts.add(menuProductDao.save(createMenuProduct(menu, product2)));
    }
}
