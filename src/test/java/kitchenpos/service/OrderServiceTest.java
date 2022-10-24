package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import kitchenpos.TestFixture;
import kitchenpos.TestUtils;
import kitchenpos.application.OrderService;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
public class OrderServiceTest {

    private final OrderService orderService;
    private final OrderTableDao orderTableDao;
    private final TestFixture testFixture;

    public OrderServiceTest(OrderService orderService, OrderTableDao orderTableDao, TestFixture testFixture) {
        this.orderService = orderService;
        this.orderTableDao = orderTableDao;
        this.testFixture = testFixture;
    }

    @DisplayName("주문을 생성할 때")
    @Nested
    class OrderCreate {

        OrderTable orderTable;
        Menu menu;

        @BeforeEach
        void setUp() {
            orderTable = orderTableDao.save(new OrderTable(null, 100, false));
            menu = testFixture.삼겹살_메뉴();
        }

        @DisplayName("주문 내에 포함되는 메뉴가")
        @Nested
        class OrderLineItemsIs {

            @DisplayName("비어있다면 예외가 발생한다.")
            @Test
            public void orderLineItemsIsEmpty() {
                Order order = new Order();
                order.setOrderTableId(orderTable.getId());
                order.setOrderLineItems(new ArrayList<>());


                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("사전에 저장되어 있지 않은 메뉴가 포함된다면 예외가 발생한다.")
            @Test
            public void orderLineItemsIsAlreadySaved() {
                Order order = new Order();
                order.setOrderTableId(orderTable.getId());
                OrderLineItem orderLineItem = new OrderLineItem(-1L, 10L);
                order.setOrderLineItems(TestUtils.of(new OrderLineItem()));

                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("주문 테이블이")
        @Nested
        class OrderTableIs {

            @DisplayName("존재하지 않는 경우 예외가 발생한다.")
            @Test
            void orderTableNotSaved() {
                Order order = new Order();
                OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 10L);
                order.setOrderLineItems(TestUtils.of(new OrderLineItem()));
                order.setOrderTableId(-1L);

                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("EMPTY 상태인 경우 예외가 발생한다.")
            @Test
            void orderTableIsEmpty() {
                Order order = new Order();
                order.setOrderTableId(orderTable.getId());
                OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 10L);
                order.setOrderLineItems(TestUtils.of(new OrderLineItem()));
                orderTable.setEmpty(true);

                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
