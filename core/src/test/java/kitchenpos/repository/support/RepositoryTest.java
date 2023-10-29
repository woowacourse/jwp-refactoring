package kitchenpos.repository.support;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.ordertable.domain.Order;
import kitchenpos.ordertable.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderLineItemRepository;
import kitchenpos.ordertable.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;

@DataJpaTest
@TestExecutionListeners(value = {DataCleaner.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class RepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    protected Menu defaultMenu() {
        MenuGroup menuGroup = prepareMenuGroup("메뉴 그룹");

        Product product1 = prepareProduct("product1", 1000L);
        Product product2 = prepareProduct("product2", 2000L);

        return prepareMenu(menuGroup.getId(), "menu1", 3000L, List.of(
            new MenuProduct(null, product1, null, 10L),
            new MenuProduct(null, product2, null, 5L)
        ));
    }

    private Menu prepareMenu(long menuGroupId, String menuName, long price, List<MenuProduct> menuProducts) {
        return menuRepository.save(
            MenuFixture.builder()
                .withId(null)
                .withName(menuName)
                .withPrice(price)
                .withMenuGroupId(menuGroupId)
                .withMenuProducts(menuProducts)
                .build());
    }

    private MenuGroup prepareMenuGroup(String menuGroupName) {
        return menuGroupRepository.save(
            new MenuGroup(null, menuGroupName)
        );
    }

    private Product prepareProduct(String productName, long price) {
        return productRepository.save(
            ProductFixture.builder()
                .withName(productName)
                .withPrice(price)
                .build());
    }

    protected Order defaultOrder() {
        Menu menu = defaultMenu();

        List<OrderTable> orderTables = prepareOrdertable();

        Order order = orderRepository.save(
            new Order(null, OrderStatus.COOKING.name(), orderTables.get(0).getId(), LocalDateTime.now(),
                new ArrayList<>()));

        orderLineItemRepository.save(new OrderLineItem(null, menu.getId(), order.getId(), 10));
        orderLineItemRepository.save(new OrderLineItem(null, menu.getId(), order.getId(), 20));

        return orderRepository.findById(order.getId()).get();
    }

    private List<OrderTable> prepareOrdertable() {
        TableGroup tableGroup = prepareTableGroup();
        return tableGroup.getOrderTables();
    }

    private TableGroup prepareTableGroup() {
        OrderTable table1 = orderTableRepository.save(new OrderTable(null, null, 10, true));
        OrderTable table2 = orderTableRepository.save(new OrderTable(null, null, 7, true));
        return tableGroupRepository.save(new TableGroup(null, LocalDateTime.now(), List.of(
            table1,
            table2
        )));
    }

    protected List<OrderTable> makeOrderTable() {
        return prepareOrdertable();
    }
}

