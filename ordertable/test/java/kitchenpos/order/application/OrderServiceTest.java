package ordertable.test.java.kitchenpos.order.application;

import static kitchenpos.fixture.TableFixture.FILL_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.exception.InvalidOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private Menu menu;
    private MenuGroup menuGroup;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));
        final Product product = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000)));
        menu = menuRepository.save(new Menu("치킨 세트 메뉴", new BigDecimal(20000), menuGroup.getId(),
                List.of(new MenuProduct(null, product.getId(), 1))));
        orderTable = orderTableRepository.save(new OrderTable(6, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(6, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTable2)));
    }

    @Nested
    class create_메서드는 {
        @Test
        void 주문을_생성한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(FILL_TABLE);
            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            final OrderRequest order = new OrderRequest(
                    orderTable.getId(),
                    List.of(orderLineItemRequest)
            );

            // when
            final Order createdOrder = orderService.create(order);

            // then
            final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
            final Order expected = new Order(
                    1L,
                    orderTable.getId(),
                    OrderStatus.MEAL.name(),
                    List.of(orderLineItem)
            );

            assertSoftly(
                    softly -> {
                        softly.assertThat(createdOrder)
                                .usingRecursiveComparison()
                                .ignoringFields("orderedTime", "orderLineItems")
                                .isEqualTo(expected);
                        softly.assertThat(createdOrder.getOrderLineItems()).hasSize(1);
                    }
            );
        }

        @Test
        void 주문_항목이_비어있으면_예외가_발생한다() {
            // given
            final OrderRequest orderRequest = new OrderRequest(
                    orderTable.getId(),
                    Collections.emptyList()
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(InvalidOrderException.class);
        }

        @Test
        void 주문_항목에_존재하지_않는_메뉴가_있으면_예외가_발생한다() {
            // given
            final long nonExistMenuId = 99L;
            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(nonExistMenuId, 1L);
            final OrderRequest order = new OrderRequest(
                    orderTable.getId(),
                    List.of(orderLineItemRequest)
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(InvalidOrderException.class);
        }

        @Test
        void 주문_항목의_메뉴가_중복되면_예외가_발생한다() {
            // given
            final OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu.getId(), 1L);
            final OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu.getId(), 2L);
            final OrderRequest order = new OrderRequest(
                    orderTable.getId(),
                    List.of(orderLineItemRequest1, orderLineItemRequest2)
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(InvalidOrderException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final long nonExistTableId = 99L;
            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            final OrderRequest order = new OrderRequest(
                    nonExistTableId,
                    List.of(orderLineItemRequest)
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(InvalidOrderException.class);
        }

        @Test
        void 테이블이_주문_불가능_상태인_경우_예외가_발생한다() {
            // given
            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            orderTable.changeEmpty(true);
            orderTableRepository.save(orderTable);
            final OrderRequest order = new OrderRequest(
                    orderTable.getId(),
                    List.of(orderLineItemRequest)
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(InvalidOrderException.class);
        }
    }

    @Test
    void list_메서드는_모든_주문을_조회한다() {
        // given
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
        final OrderTable orderTable = orderTableRepository.save(FILL_TABLE);
        final OrderRequest order1 = new OrderRequest(
                orderTable.getId(),
                List.of(orderLineItemRequest)
        );
        final OrderRequest order2 = new OrderRequest(
                orderTable.getId(),
                List.of(orderLineItemRequest)
        );
        final Order createdOrder1 = orderService.create(order1);
        final Order createdOrder2 = orderService.create(order2);

        // when
        final List<Order> orders = orderService.list();

        // then
        System.out.println(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
        assertThat(orders)
                .usingRecursiveComparison()
                .isEqualTo(List.of(createdOrder1, createdOrder2));
    }

    @Nested
    class changeOrderStatus_메서드는 {
        @Test
        void 주문_상태를_변경한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(FILL_TABLE);
            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            final OrderRequest order = new OrderRequest(
                    orderTable.getId(),
                    List.of(orderLineItemRequest)
            );
            final Order createdOrder = orderService.create(order);

            // when
            final OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest("MEAL");
            final Order actual = orderService.changeOrderStatus(createdOrder.getId(), orderStatusChangeRequest);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 존재하지_않는_주문의_상태를_변경하면_예외가_발생한다() {
            // given
            final long nonExistOrderId = 99L;

            // when & then
            final OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest("MEAL");
            assertThatThrownBy(() -> orderService.changeOrderStatus(nonExistOrderId, orderStatusChangeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 계산이_완료된_주문의_상태를_변경하면_예외가_발생한다() {
            // given
            final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
            final Order order = new Order(
                    orderTable.getId(),
                    OrderStatus.COMPLETION.name(),
                    LocalDateTime.now(),
                    List.of(orderLineItem)
            );
            final Order completedOrder = orderRepository.save(order);

            // when & then
            final OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest("MEAL");
            assertThatThrownBy(() -> orderService.changeOrderStatus(completedOrder.getId(), orderStatusChangeRequest))
                    .isInstanceOf(InvalidOrderException.class);
        }
    }
}
