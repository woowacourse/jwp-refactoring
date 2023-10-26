package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.repository.OrderTableRepository;
import kitchenpos.domain.table.service.TableService;
import kitchenpos.domain.table.service.dto.OrderTableCreateRequest;
import kitchenpos.domain.table.service.dto.OrderTableResponse;
import kitchenpos.domain.table.service.dto.OrderTableUpdateGuestsRequest;
import kitchenpos.domain.table.service.dto.OrderTableUpdateRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Nested
    class Create {

        @Test
        void 주문_테이블을_만들_수_있다() {
            // given
            final OrderTableCreateRequest request = new OrderTableCreateRequest(6, true);
            final OrderTable spyOrderTable = spy(new OrderTable(null, request.getNumberOfGuests(), request.getEmpty()));
            given(orderTableRepository.save(any(OrderTable.class))).willReturn(spyOrderTable);
            final long savedId = 1L;
            given(spyOrderTable.getId()).willReturn(savedId);

            // when
            final OrderTableResponse actual = tableService.create(request);

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                    () -> assertThat(actual.getEmpty()).isEqualTo(request.getEmpty())
            );
        }
    }

    @Nested
    class FindAll {

        @Test
        void 주문_테이블을_전체_조회할_수_있다() {
            // when
            tableService.list();

            // then
            verify(orderTableRepository, only()).findAll();
        }
    }

    @Nested
    class ChangeEmpty {
        @Test
        void 주문_테이블을_비어있는_상태로_만들_수_있다() {
            // given
            final OrderTable spyOrderTable = spy(new OrderTable(5, true));
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(spyOrderTable));

            final long orderTableId = 1L;
            final Order order = new Order(orderTableId, COMPLETION, LocalDateTime.now(), new OrderLineItems());
            given(orderRepository.findByOrderTableId(orderTableId)).willReturn(Optional.ofNullable(order));

            // when, then
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(false);
            tableService.changeEmpty(orderTableId, request);
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"MEAL","COOKING"})
        void 주문상태가_요리중이거나_식사중이면_예외가_발생한다(final OrderStatus orderStatus) {
            // given
            final OrderTable spyOrderTable = spy(new OrderTable(5, true));
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(spyOrderTable));

            final long orderTableId = 1L;
            final Order order = new Order(orderTableId, orderStatus, LocalDateTime.now(), new OrderLineItems());
            given(orderRepository.findByOrderTableId(orderTableId)).willReturn(Optional.ofNullable(order));

            // when, then
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(false);
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class ChangeNumberOfGuests {

        @Test
        void 게스트의_수를_변경할_수_있다() {
            // given
            final OrderTable expected = spy(new OrderTable(1, true));
            expected.changeEmpty(false);
            final long orderTableId = 1L;
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.ofNullable(expected));

            // when
            final OrderTableUpdateGuestsRequest request = new OrderTableUpdateGuestsRequest(5);
            assertThatCode(() -> tableService.changeNumberOfGuests(orderTableId, request))
                    .doesNotThrowAnyException();
        }

        @Test
        void 게스트의_수가_0이면_예외가_발생한다() {
            // when, then
            final long orderTableId = 1L;
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, new OrderTableUpdateGuestsRequest(0)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있으면_예외가_발생한다() {
            // given
            final OrderTable target = spy(new OrderTable(5, true));
            final long orderTableId = 1L;
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.ofNullable(target));

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, new OrderTableUpdateGuestsRequest(3)))
                    .isInstanceOf(IllegalStateException.class);
        }
    }
}
