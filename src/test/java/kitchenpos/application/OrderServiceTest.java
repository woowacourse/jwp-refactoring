package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.application.fixture.MenuFixture.createMenu;
import static kitchenpos.application.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.application.fixture.OrderFixture.*;
import static kitchenpos.application.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.application.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.application.fixture.TableGroupFixture.createTableGroup;
import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ServiceIntegrationTest
@DisplayName("주문 서비스")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateOrder {
        private Order request;

        private Order subject() {
            return orderService.create(request);
        }

        @Nested
        @DisplayName("주문 테이블, 주문 항목들이 주어지면")
        class WithOrderTableIdAndOrderLineItems {
            @BeforeEach
            void setUp() {
                TableGroup tableGroup = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList()));
                OrderTable orderTable = orderTableDao.save(createOrderTable(null, false, tableGroup.getId(), 1));

                Long menuGroupId = menuGroupDao.save(createMenuGroup(null, "추천메뉴")).getId();
                Menu menu1 = menuDao.save(createMenu(null, "후라이드+후라이드", BigDecimal.valueOf(1000L), menuGroupId, null));
                Menu menu2 = menuDao.save(createMenu(null, "후라이드+양념치킨", BigDecimal.valueOf(1000L), menuGroupId, null));
                List<OrderLineItem> orderLineItems =
                        Arrays.asList(createOrderLineItem(menu1.getId(), 2L), createOrderLineItem(menu2.getId(), 1L));
                request = createOrderRequest(orderLineItems, orderTable.getId());
            }

            @Test
            @DisplayName("초기상태가 조리 중인 주문을 생성한다")
            void createOrder() {
                Order result = subject();

                assertAll(
                        () -> assertThat(result.getId()).isNotNull(),
                        () -> assertThat(result.getOrderedTime()).isNotNull(),
                        () -> assertThat(result.getOrderTableId()).isNotNull(),
                        () -> assertThat(result.getOrderStatus()).isEqualTo(COOKING.name()),
                        () -> assertThat(result.getOrderLineItems()).hasSize(2)
                );
            }
        }

        @Nested
        @DisplayName("요청에서 주문 항목이 비어있을 경우")
        class WithEmptyOrderLineItems {
            @BeforeEach
            void setUp() {
                request = createOrderRequest(null, 5L);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateOrder.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("요청의 주문 테이블 id에 해당하는 주문 테이블이 없는 경우")
        class WhenNotExistIOrderTable {
            @BeforeEach
            void setUp() {
                Long menuGroupId = menuGroupDao.save(createMenuGroup(null, "추천메뉴")).getId();
                Menu menu1 = menuDao.save(createMenu(null, "후라이드+후라이드", BigDecimal.valueOf(1000L), menuGroupId, null));
                Menu menu2 = menuDao.save(createMenu(null, "후라이드+양념치킨", BigDecimal.valueOf(1000L), menuGroupId, null));

                List<OrderLineItem> orderLineItems =
                        Arrays.asList(createOrderLineItem(menu1.getId(), 2L), createOrderLineItem(menu2.getId(), 1L));
                request = createOrderRequest(orderLineItems, 0L);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateOrder.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("조회 메서드는")
    class FindOrder {
        private List<Order> subject() {
            return orderService.list();
        }

        @Nested
        @DisplayName("주문과 주문 항목들이 저장되어 있으면")
        class WhenOrderAndOrderLineItemsSaved {
            private List<Order> orders;
            private List<OrderLineItem> orderLineItems;

            @BeforeEach
            void setUp() {
                TableGroup tableGroup = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList()));
                OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, false, tableGroup.getId(), 1));
                OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, false, tableGroup.getId(), 2));
                OrderTable orderTable3 = orderTableDao.save(createOrderTable(null, false, tableGroup.getId(), 3));

                Long menuGroupId = menuGroupDao.save(createMenuGroup(null, "추천메뉴")).getId();
                Menu menu1 = menuDao.save(createMenu(null, "후라이드+후라이드", BigDecimal.valueOf(1000L), menuGroupId, null));
                Menu menu2 = menuDao.save(createMenu(null, "후라이드+양념치킨", BigDecimal.valueOf(1000L), menuGroupId, null));

                orders = Arrays.asList(
                        createOrder(null, COOKING, orderTable1.getId(), LocalDateTime.now()),
                        createOrder(null, MEAL, orderTable2.getId(), LocalDateTime.now()),
                        createOrder(null, COOKING, orderTable3.getId(), LocalDateTime.now())
                );
                for (Order order : orders) {
                    Order persisted = orderDao.save(order);
                    order.setId(persisted.getId());
                }

                orderLineItems = Arrays.asList(
                        createOrderLineItem(orders.get(0).getId(), menu1.getId(), 1L),
                        createOrderLineItem(orders.get(1).getId(), menu1.getId(), 2L),
                        createOrderLineItem(orders.get(1).getId(), menu2.getId(), 1L),
                        createOrderLineItem(orders.get(2).getId(), menu1.getId(), 1L),
                        createOrderLineItem(orders.get(2).getId(), menu2.getId(), 2L)
                );
                for (OrderLineItem orderLineItem : orderLineItems) {
                    OrderLineItem persisted = orderLineItemDao.save(orderLineItem);
                    orderLineItem.setSeq(persisted.getSeq());
                }
            }

            @Test
            @DisplayName("주문과 주문 항목들을 조회한다")
            void findOrderAndOrderLineItems() {
                List<Order> result = subject();

                assertThat(result)
                        .usingElementComparatorIgnoringFields("orderLineItems")
                        .containsAll(orders);
                assertThat(result).flatExtracting(Order::getOrderLineItems)
                        .usingFieldByFieldElementComparator()
                        .isEqualTo(orderLineItems);
            }
        }
    }

    @Nested
    @DisplayName("수정 메서드는")
    class ChangeOrder {
        private Long orderId;
        private Order request;

        private Order subject() {
            return orderService.changeOrderStatus(orderId, request);
        }

        @Nested
        @DisplayName("주문이 저장되어 있고, 주문 id와 주문 상태가 주어지면")
        class WhenOrderSavedAndWithOrderIdAndOrderStatus {
            @BeforeEach
            void setUp() {
                TableGroup tableGroup = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList()));
                OrderTable orderTable = orderTableDao.save(createOrderTable(null, false, tableGroup.getId(), 1));
                orderId = orderDao.save(createOrder(null, COOKING, orderTable.getId(), LocalDateTime.now())).getId();

                request = updateOrderRequest(MEAL);
            }

            @Test
            @DisplayName("주문의 상태를 수정하고 바뀐 주문을 반환한다")
            void findOrderAndOrderLineItems() {
                Order result = subject();

                assertThat(result).isEqualToIgnoringGivenFields(orderDao.findById(orderId).get(), "orderStatus", "orderLineItems");
                assertThat(result.getOrderStatus()).isEqualTo(MEAL.name());
            }
        }

        @Nested
        @DisplayName("주문 id에 해당하는 주문이 없을 경우")
        class WhenNotExistOrder {
            @BeforeEach
            void setUp() {
                orderId = 0L;
                request = updateOrderRequest(MEAL);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(ChangeOrder.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("기존 주문 상태가 '계산 완료'인 경우")
        class WhenCompletedOrder {
            @BeforeEach
            void setUp() {
                TableGroup tableGroup = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList()));
                OrderTable orderTable = orderTableDao.save(createOrderTable(null, false, tableGroup.getId(), 1));
                orderId = orderDao.save(createOrder(null, COMPLETION, orderTable.getId(), LocalDateTime.now())).getId();

                request = updateOrderRequest(COOKING);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(ChangeOrder.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}