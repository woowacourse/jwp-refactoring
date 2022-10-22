package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OrderServiceTest {

    @MockBean
    private MenuDao menuDao;

    @MockBean
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);

        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(menuDao.countByIdIn(any())).willReturn(1L);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        final Order savedOrder = orderService.create(order);
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull()
        );
    }

    @Test
    @DisplayName("주문을 하면 COOKING 상태로 바뀌어야 한다.")
    void changeStatus() {
        final Order savedOrder = orderService.create(order);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
    }

    @Test
    @DisplayName("주문시간이 등록되어야 한다.")
    void createCurrentOrderDate() {
        final Order savedOrder = orderService.create(order);
        assertThat(savedOrder.getOrderedTime()).isNotNull();
    }

    @ParameterizedTest(name = "{1} 예외가 발생한다.")
    @MethodSource("invalidParams")
    void createWithInvalidOrder(final Order order, final String testName) {
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidParams() {
        return Stream.of(
                Arguments.of(new Order(1L, null),
                        "아이템 목록이 비어있을 경우"),
                Arguments.of(new Order(1L, Arrays.asList(new OrderLineItem(), new OrderLineItem())),
                        "주문 상품 목록에 등록되지 않은 메뉴가 존재할 경우")
        );
    }
}
