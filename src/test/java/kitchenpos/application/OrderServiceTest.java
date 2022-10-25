package kitchenpos.application;

import static kitchenpos.application.fixture.OrderFixture.INVALID_ORDER_ID;
import static kitchenpos.application.fixture.OrderFixture.ORDER_LINE_ITEMS;
import static kitchenpos.application.fixture.OrderFixture.ORDER_TABLE_EMPTY_ID;
import static kitchenpos.application.fixture.OrderFixture.ORDER_TABLE_NOT_EMPTY_ID;
import static kitchenpos.application.fixture.OrderFixture.UNSAVED_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        Order savedOrder = orderService.create(UNSAVED_ORDER);
        Optional<Order> foundOrder = orderDao.findById(savedOrder.getId());

        assertThat(foundOrder).isPresent();
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        int numberOfSavedOrderBeforeCreate = orderService.list().size();
        orderService.create(UNSAVED_ORDER);

        int numberOfSavedOrder = orderService.list().size();

        assertThat(numberOfSavedOrderBeforeCreate + 1).isEqualTo(numberOfSavedOrder);
    }

    @DisplayName("주문엔 주문내역과 테이블아이디는 반드시 있어야 한다. 그렇지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("argsOfCreateExceptionField")
    void create_Exception_Field(Order invalidOrder) {
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(Exception.class);
    }

    static Stream<Arguments> argsOfCreateExceptionField() {
        return Stream.of(
                Arguments.of(new Order(null, ORDER_LINE_ITEMS)),
                Arguments.of(new Order(ORDER_TABLE_NOT_EMPTY_ID, null))
        );
    }

    @DisplayName("주문의 테이블아이디에 해당하는 테이블이 EMPTY상태이면 예외가 발생한다.")
    @Test
    void create_Exception_OrderTable_Empty() {
        Order orderTableEmptyOrder = new Order(ORDER_TABLE_EMPTY_ID, ORDER_LINE_ITEMS);
        assertThatThrownBy(() -> orderService.create(orderTableEmptyOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus(String orderStatus) {
        Order savedOrder = orderService.create(UNSAVED_ORDER);
        UNSAVED_ORDER.setOrderStatus(orderStatus);
        orderService.changeOrderStatus(savedOrder.getId(), UNSAVED_ORDER);
        Order foundOrder = orderService.list().stream()
                .filter(it -> savedOrder.getId().equals(it.getId()))
                .findAny().get();

        assertThat(foundOrder.getOrderStatus()).isEqualTo(orderStatus);
    }

    @DisplayName("주문 상태는 COOKING, MEAL, COMPLETION 뿐인다. 이외의 값은 예외가 발생한다.")
    @Test
    void changeOrderStatus_Exception_Invalid_Value() {
        Order savedOrder = orderService.create(UNSAVED_ORDER);
        UNSAVED_ORDER.setOrderStatus("WRONG_STATUS");
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), UNSAVED_ORDER))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료 상태의 주문은 상태를 바꿀 수 없다.")
    @Test
    void changeOrderStatus_Excpetion_Change_Unavailable() {
        Order savedOrder = orderService.create(UNSAVED_ORDER);
        UNSAVED_ORDER.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(savedOrder.getId(), UNSAVED_ORDER);
        UNSAVED_ORDER.equals(OrderStatus.COOKING.name());
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), UNSAVED_ORDER))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 주문의 상태를 변경하려 하면 예외가 발생한다.")
    @Test
    void changeOrderStatus_Exception_Invalid_Order() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(INVALID_ORDER_ID, UNSAVED_ORDER))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
