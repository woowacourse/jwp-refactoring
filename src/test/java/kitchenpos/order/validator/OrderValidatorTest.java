package kitchenpos.order.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.exception.InvalidTableOrderException;
import kitchenpos.order.exception.MenuNotEnoughException;
import kitchenpos.ordertable.repository.TableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderValidatorTest {

    private final TableRepository tableRepository = Mockito.mock(TableRepository.class);
    private final OrderValidator orderValidator = new OrderValidator(tableRepository);
    private final OrderLineItem orderLineItem = new OrderLineItem("메뉴1", new Price(new BigDecimal(1000)),
            new Quantity(1L));

    @DisplayName("존재하지 않거나 empty 상태인 테이블에 주문을 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void validate_Exception_NotFoundOrNotEmptyTable() {
        Long invalidOrderTableId = 1L;
        Mockito.when(tableRepository.existsByIdAndAndEmptyIsFalse(invalidOrderTableId))
                .thenReturn(false);

        assertThatThrownBy(() -> orderValidator.validateCreation(invalidOrderTableId, List.of(orderLineItem)))
                .isInstanceOf(InvalidTableOrderException.class);
    }

    @DisplayName("주문 항목에 메뉴가 하나도 포함되어 있지 않다면 예외를 발생시킨다.")
    @Test
    void validate_Exception_NotEnoughMenu() {
        Mockito.when(tableRepository.existsByIdAndAndEmptyIsFalse(1L))
                .thenReturn(true);

        assertThatThrownBy(() -> orderValidator.validateCreation(1L, Collections.emptyList()))
                .isInstanceOf(MenuNotEnoughException.class);
    }

    @DisplayName("empty가 false 상태의 테이블에 메뉴를 포함해서 주문을 할 수 있다.")
    @Test
    void validate_Pass() {
        Mockito.when(tableRepository.existsByIdAndAndEmptyIsFalse(1L))
                .thenReturn(true);

        assertThatCode(() -> orderValidator.validateCreation(1L, List.of(orderLineItem)))
                .doesNotThrowAnyException();
    }
}
