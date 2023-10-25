package kitchenpos.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import kitchenpos.config.RepositoryTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class OrderLineItemRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    private Order order;

    private Menu menu;

    @BeforeEach
    void setUp() {
        TableGroup tableGroupEntity = TableGroup.builder()
                .orderTables(Collections.emptyList())
                .build();
        TableGroup tableGroup = tableGroupRepository.save(tableGroupEntity);

        OrderTable orderTableEntity = OrderTable.builder()
                .numberOfGuests(5)
                .tableGroup(tableGroup)
                .build();
        OrderTable orderTable = orderTableRepository.save(orderTableEntity);

        Order orderEntity = Order.builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .build();
        order = orderRepository.save(orderEntity);

        MenuGroup menuGroupEntity = MenuGroup.builder()
                .name("샐러드")
                .build();
        MenuGroup menuGroup = menuGroupRepository.save(menuGroupEntity);

        Menu menuEntity = Menu.builder()
                .name("닭가슴살 샐러드")
                .menuGroupId(menuGroup.getId())
                .price(1_000_000)
                .build();
        menu = menuRepository.save(menuEntity);
    }

    @Test
    void 주문_아이템_엔티티를_저장한다() {
        OrderLineItem orderLineItemEntity = createOrderLineItem();

        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItemEntity);

        assertThat(savedOrderLineItem.getSeq()).isPositive();
    }

    @Test
    void 주문_아이템_엔티티를_조회한다() {
        OrderLineItem orderLineItemEntity = createOrderLineItem();
        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItemEntity);

        assertThat(orderLineItemRepository.findById(savedOrderLineItem.getSeq())).isPresent();
    }

    @Test
    void 모든_주문_아이템_엔티티를_조회한다() {
        OrderLineItem orderLineItemEntityA = createOrderLineItem();
        OrderLineItem orderLineItemEntityB = createOrderLineItem();
        OrderLineItem savedOrderLineItemA = orderLineItemRepository.save(orderLineItemEntityA);
        OrderLineItem savedOrderLineItemB = orderLineItemRepository.save(orderLineItemEntityB);

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAll();

        assertThat(orderLineItems).usingRecursiveFieldByFieldElementComparatorOnFields("seq")
                .contains(savedOrderLineItemA, savedOrderLineItemB);
    }

    @Test
    void 주문에_해당하는_모든_주문_아이템_엔티티를_조회한다() {
        OrderLineItem orderLineItemEntityA = createOrderLineItem();
        OrderLineItem orderLineItemEntityB = createOrderLineItem();
        OrderLineItem saveOrderLineItemA = orderLineItemRepository.save(orderLineItemEntityA);
        OrderLineItem saveOrderLineItemB = orderLineItemRepository.save(orderLineItemEntityB);

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());

        assertThat(orderLineItems).usingRecursiveFieldByFieldElementComparatorOnFields("seq")
                .contains(saveOrderLineItemA, saveOrderLineItemB);
    }

    private OrderLineItem createOrderLineItem() {
        return OrderLineItem.builder()
                .menu(menu)
                .order(order)
                .quantity(10)
                .build();
    }
}
