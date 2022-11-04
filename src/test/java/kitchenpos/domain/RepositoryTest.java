package kitchenpos.domain;

import static kitchenpos.domain.DomainTestFixture.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class RepositoryTest {

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderLineItemRepository orderLineItemRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    protected Product product1;
    protected Product product2;
    protected OrderTable orderTable1;
    protected OrderTable orderTable2;
    protected MenuGroup menuGroup;
    protected Menu menu;

    @BeforeEach
    void setUp() {
        product1 = productRepository.save(Product.create("상품1", BigDecimal.valueOf(1000L)));
        product2 = productRepository.save(Product.create("상품2", BigDecimal.valueOf(1500L)));
        orderTable1 = orderTableRepository.save(OrderTable.create());
        orderTable2 = orderTableRepository.save(OrderTable.create());
        menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));
        menu = createMenu();
    }

    private Menu createMenu() {
        final MenuProduct menuProduct1 = new MenuProduct(product1.getId(), 1);
        final MenuProduct menuProduct2 = new MenuProduct(product2.getId(), 1);
        final Menu menu = Menu.create(
                "테스트메뉴",
                BigDecimal.valueOf(2500L),
                menuGroup.getId(),
                List.of(menuProduct1, menuProduct2)
        );

        return menuRepository.save(menu);
    }
}
