package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.repository.OrderTableRepository;

@SpringBootTest
class OrderValidatorTest {

    private static final List<OrderLineItemDto> orderLineItems = new ArrayList<OrderLineItemDto>() {{
        add(new OrderLineItemDto("menuName", BigDecimal.valueOf(1000L), 1L));
    }};

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Nested
    @DisplayName("validateCreateOrder")
    class ValidateCreateOrderMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 예외가 발생하지 않는다.")
        void createOrder() {
            assertDoesNotThrow(() -> orderValidator.validateCreateOrder(
                    createAndSaveOrderTable().getId(),
                    orderLineItems
                )
            );
        }

        @Test
        @DisplayName("주문 테이블이 비어있는 경우 예외가 발생한다.")
        void nullOrderTableId() {
            assertThatThrownBy(() -> orderValidator.validateCreateOrder(
                null,
                orderLineItems
            ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블은 비어있을 수 없습니다.");
        }

        @Test
        @DisplayName("존재하지 않는 주문 테이블인 경우 예외가 발생한다.")
        void invalidOrderTableId() {
            assertThatThrownBy(() -> orderValidator.validateCreateOrder(
                0L,
                orderLineItems
            ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문 테이블 입니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("orderLineItems가 비어있는 경우 예외가 발생한다.")
        void emptyOrderItemLines(List<OrderLineItemDto> orderLineItems) {
            assertThatThrownBy(() -> orderValidator.validateCreateOrder(
                    createAndSaveOrderTable().getId(),
                    orderLineItems
                )
            )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목은 비어있을 수 없습니다.");
        }

    }

    @Nested
    @DisplayName("validateChangeStatus()")
    class ValidateChangeStatusMethod {

        @Test
        @DisplayName("COMPLETION 상태인 order를 변경하려는 경우 예외가 발생한다.")
        void completionStatus() {
            // given
            Order order = new Order(
                createAndSaveOrderTable().getId(),
                orderLineItems,
                orderValidator);

            order.changeStatus(OrderStatus.COMPLETION.name(), orderValidator);

            // when, then
            assertThatThrownBy(() -> order.changeStatus("COOKING", orderValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문 상태를 변경할 수 없습니다.");
        }

    }

    private OrderTable createAndSaveOrderTable() {
        OrderTable orderTable = new OrderTable(10, false);
        return orderTableRepository.save(orderTable);
    }

}
