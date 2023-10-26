package kitchenpos.repository.support;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
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

        Menu menu = prepareMenu(menuGroup.getId(), "menu1", 3000L);

        Product product1 = prepareProduct("product1", 1000L);
        Product product2 = prepareProduct("product2", 2000L);

        prepareMenuProduct(menu, product1, 2);
        prepareMenuProduct(menu, product2, 1);

        return menuRepository.findById(menu.getId()).get();
    }

    private Menu prepareMenu(long menuGroupId, String menuName, long price) {
        return menuRepository.save(
            MenuFixture.builder()
                .withId(null)
                .withName(menuName)
                .withPrice(price)
                .withMenuGroupId(menuGroupId)
                .build());
    }

    private MenuProduct prepareMenuProduct(Menu menu, Product product, int quantity) {
        return menuProductRepository.save(MenuProductFixture.builder()
            .withMenuId(menu.getId())
            .withProductId(product.getId())
            .withMenuId(1L)
            .withQuantity(quantity)
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
            new Order(null, orderTables.get(0).getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>()));

        orderLineItemRepository.save(new OrderLineItem(null, menu.getId(), order, 10));
        orderLineItemRepository.save(new OrderLineItem(null, menu.getId(), order, 20));

        return orderRepository.findById(order.getId()).get();
    }

    private List<OrderTable> prepareOrdertable() {
        TableGroup tableGroup = prepareTableGroup();
        return tableGroup.getOrderTables();
    }

    private TableGroup prepareTableGroup() {
        return tableGroupRepository.save(new TableGroup(null, LocalDateTime.now(), List.of(
            new OrderTable(null, null, 10, true),
            new OrderTable(null, null, 7, true)
        )));
    }

    protected List<OrderTable> makeOrderTable(){
        return prepareOrdertable();
    }
}

