package kitchenpos.application;

import kitchenpos.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    void 주문하려는_상품이_비어있으면_예외발생() {
        assertThatThrownBy(() -> orderValidator.validate(null, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_테이블이_존재하지_않으면_예외발생() {
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        OrderLineItem orderLineItem = new OrderLineItem(1L, 3);

        Long invalidOrderTableId = 9999999L;
        Order order = new Order(invalidOrderTableId);
        assertThatThrownBy(() -> orderValidator.validate(order, List.of(orderLineItem)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_테이블이_빈_테이블이면_예외발생() {
        OrderTable notEmptyTable = new OrderTable(1L, 3, false);
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(notEmptyTable));

        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2);
        Order order = new Order(1L);

        given(menuRepository.countByIdIn(anyList()))
                .willReturn(1L);

        assertThatThrownBy(() -> orderValidator.validate(order, List.of(orderLineItem1, orderLineItem2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
