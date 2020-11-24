package kitchenpos.repository;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OrderLineItemRepositoryTest {
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    OrderTable orderTable;

    Order order;

    MenuGroup menuGroup;

    Menu menu;

    @BeforeEach
    void setup() {
        orderTable = orderTableRepository.save(createOrderTable(null, true, 0, null));
        order = orderRepository.save(
            createOrder(null, LocalDateTime.now(), OrderStatus.COOKING, orderTable.getId()));
        menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
        menu = menuRepository.save(createMenu(null, "메뉴", 0L, menuGroup.getId()));
    }

    @DisplayName("주문 항목을 저장할 수 있다.")
    @Test
    void save() {
        OrderLineItem orderLineItem = createOrderLineItem(null, order.getId(), menu.getId(), 0);

        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);

        assertAll(
            () -> assertThat(savedOrderLineItem.getSeq()).isNotNull(),
            () -> assertThat(savedOrderLineItem).isEqualToIgnoringGivenFields(orderLineItem, "seq")
        );
    }

    @DisplayName("주문 항목 아이디로 주문 항목을 조회할 수 있다.")
    @Test
    void findById() {
        OrderTable orderTable = orderTableRepository.save(createOrderTable(null, true, 0, null));
        Order order = orderRepository
            .save(createOrder(null, LocalDateTime.now(), OrderStatus.COOKING,
                orderTable.getId()));
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
        Menu menu = menuRepository.save(createMenu(null, "메뉴", 0L, menuGroup.getId()));
        OrderLineItem orderLineItem = orderLineItemRepository
            .save(createOrderLineItem(null, order.getId(), menu.getId(), 0));

        Optional<OrderLineItem> foundOrderLineItem = orderLineItemRepository
            .findById(orderLineItem.getSeq());

        assertThat(foundOrderLineItem.get()).isEqualToComparingFieldByField(orderLineItem);
    }

    @DisplayName("주문 항목 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
            orderLineItemRepository.save(createOrderLineItem(null, order.getId(), menu.getId(), 0)),
            orderLineItemRepository.save(createOrderLineItem(null, order.getId(), menu.getId(), 0)),
            orderLineItemRepository.save(createOrderLineItem(null, order.getId(), menu.getId(), 0))
        );

        List<OrderLineItem> allOrderLineItems = orderLineItemRepository.findAll();

        assertThat(allOrderLineItems).usingFieldByFieldElementComparator()
            .containsAll(orderLineItems);
    }

    @DisplayName("주문 아이디로 주문 항목 목록을 조회할 수 있다.")
    @Test
    void findAllByOrderId() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
            orderLineItemRepository.save(createOrderLineItem(null, order.getId(), menu.getId(), 0)),
            orderLineItemRepository.save(createOrderLineItem(null, order.getId(), menu.getId(), 0)),
            orderLineItemRepository.save(createOrderLineItem(null, order.getId(), menu.getId(), 0))
        );

        List<OrderLineItem> allOrderLineItemsByOrderId = orderLineItemRepository
            .findAllByOrderId(order.getId());

        assertThat(allOrderLineItemsByOrderId).usingFieldByFieldElementComparator()
            .isEqualTo(orderLineItems);
    }
}
