package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.KitchenPosException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderTableServiceTest {

    @InjectMocks
    OrderTableService orderTableService;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    OrderRepository orderRepository;

    @Nested
    class create {

        @Test
        void 성공() {
            // given
            var request = new OrderTableCreateRequest(false, 4885);
            given(orderTableRepository.save(any(OrderTable.class)))
                .willAnswer(invoke -> invoke.getArgument(0));

            // when
            var orderTableResponse = orderTableService.create(request);

            // then
            assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(4885);
            assertThat(orderTableResponse.getTableGroupId()).isNull();
        }
    }

    @Nested
    class changeEmpty {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, mode = Mode.EXCLUDE, names = {"COMPLETION"})
        void 테이블의_주문이_계산_완료가_아니면_예외(OrderStatus orderStatus) {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");

            Order order = new Order(1L, orderStatus, orderedTime, orderTable);
            given(orderRepository.findByOrderTableId(anyLong()))
                .willReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderTableService.changeEmpty(1L, true))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("계산 완료 상태가 아닌 주문이 있는 테이블은 상태를 변경할 수 없습니다. orderTableId=1");
        }

        @Test
        void 주문_테이블_식별자에_대한_주문_테이블이_없으면_예외() {
            // given
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderTableService.changeEmpty(1L, true))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("해당 주문 테이블이 없습니다. orderTableId=1");
        }

        @Test
        void 주문_테이블_식별자에_대한_주문이_없으면_예외() {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
            given(orderRepository.findByOrderTableId(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderTableService.changeEmpty(1L, true))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("해당 주문이 없습니다. orderTableId=1");
        }

        @Test
        void 테이블의_주문이_계산_완료이면_성공() {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");

            Order order = new Order(1L, OrderStatus.COMPLETION, orderedTime, orderTable);
            given(orderRepository.findByOrderTableId(anyLong()))
                .willReturn(Optional.of(order));

            // when
            orderTableService.changeEmpty(1L, true);

            // then
            assertThat(orderTable.isEmpty()).isTrue();
        }
    }
}
