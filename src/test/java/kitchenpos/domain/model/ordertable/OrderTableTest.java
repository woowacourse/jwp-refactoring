package kitchenpos.domain.model.ordertable;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.model.order.OrderRepository;
import kitchenpos.domain.model.order.OrderStatus;

@ExtendWith(MockitoExtension.class)
class OrderTableTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderTableChangeEmptyService orderTableChangeEmptyService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 0, false);
        orderTable2 = new OrderTable(2L, 1L, 0, true);
    }

    @DisplayName("테이블 주문 여부 변경")
    @TestFactory
    Stream<DynamicTest> changeEmpty() {
        return Stream.of(
                dynamicTest("테이블 주문 여부를 변경한다.", this::changeEmptySuccess),
                dynamicTest("단체 지정은 존재하지 않아야한다.", this::orderTableHasTableGroupId),
                dynamicTest("테이블에 모든 주문은 완료 상태이어야 한다.", this::invalidOrderStatus)
        );
    }

    private void changeEmptySuccess() {
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        orderTable1.changeEmpty(true, orderTableChangeEmptyService);

        assertThat(orderTable1.isEmpty()).isEqualTo(true);
    }

    private void orderTableHasTableGroupId() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable2.changeEmpty(true, orderTableChangeEmptyService));
    }

    private void invalidOrderStatus() {
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable1.changeEmpty(true, orderTableChangeEmptyService));
    }

    @DisplayName("빈 테이블은 손님 수를 변경할 수 없다.")
    @TestFactory
    Stream<DynamicTest> changeNumberOfGuests() {
        return Stream.of(
                dynamicTest("손님 수 변경 성공", () -> {
                    int numberOfGuests = orderTable1.getNumberOfGuests();
                    orderTable1.changeNumberOfGuests(orderTable1.getNumberOfGuests() + 1);

                    assertThat(orderTable1.getNumberOfGuests()).isEqualTo(numberOfGuests + 1);
                }),
                dynamicTest("빈 테이블은 손님 수를 변경할 수 없다.", () -> {
                    assertThatIllegalArgumentException()
                            .isThrownBy(() -> orderTable2.changeNumberOfGuests(
                                    orderTable2.getNumberOfGuests() + 1));
                })
        );
    }
}