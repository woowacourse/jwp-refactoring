package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderFixture.계산_완료_상태_주문;
import static kitchenpos.order.domain.OrderFixture.조리_상태_주문;

import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderStatusValidateEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class OrderStatusValidateEventHandlerTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderStatusValidateEventHandler eventHandler;

    @Test
    void 테이블에_조리_혹은_식사_상태인_주문이_없다면_예외를_던지지_않는다() {
        // given
        Long orderTableId = 1L;

        BDDMockito.given(orderRepository.findAllByOrderTableId(orderTableId))
                .willReturn(List.of(계산_완료_상태_주문()));

        OrderStatusValidateEvent event = new OrderStatusValidateEvent(orderTableId);

        // expect
        Assertions.assertThatNoException().isThrownBy(() -> eventHandler.validateTableHasCookingOrMealOrder(event));
    }

    @Test
    void 테이블에_조리_혹은_식사_상태인_주문이_있다면_예외를_던진다() {
        // given
        Long orderTableId = 1L;

        BDDMockito.given(orderRepository.findAllByOrderTableId(orderTableId))
                .willReturn(List.of(계산_완료_상태_주문(), 조리_상태_주문()));

        OrderStatusValidateEvent event = new OrderStatusValidateEvent(orderTableId);

        // expect
        Assertions.assertThatThrownBy(() -> eventHandler.validateTableHasCookingOrMealOrder(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 혹은 식사 중인 주문이 존재하는 주문 테이블은 비어있는지 여부를 변경할 수 없습니다.");
    }
}
