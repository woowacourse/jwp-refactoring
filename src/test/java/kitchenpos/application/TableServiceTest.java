package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableEmptyChangeRequest;
import kitchenpos.dto.request.OrderTableGuestChangeRequest;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Captor
    ArgumentCaptor<OrderTable> orderTableArgumentCaptor;

    @Test
    void 테이블_생성() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(100, true);

        given(orderTableRepository.save(any()))
            .willReturn(
                OrderTableFixture.builder()
                    .build());

        // when
        tableService.create(request);

        // then
        assertSoftly(softAssertions -> {
            verify(orderTableRepository, times(1)).save(orderTableArgumentCaptor.capture());
            OrderTable savedOrderTabled = orderTableArgumentCaptor.getValue();
            assertThat(savedOrderTabled.getId()).isNull();
            assertThat(savedOrderTabled.getTableGroup()).isNull();
            assertThat(savedOrderTabled.isEmpty()).isTrue();
            assertThat(savedOrderTabled.getNumberOfGuests()).isEqualTo(100);
        });
    }

    @Nested
    class 테이블이_빈_상태를_변경한다 {

        private long orderTableId = 1L;

        @Test
        void 존재하지_않는_테이블이면_예외() {
            // given
            OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(true);

            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_테이블_그룹에_포함되어_있다면_예외() {
            // given
            OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(true);

            OrderTable reqeustOrderTable = OrderTableFixture.builder()
                .withTableGroupId(1L)
                .build();
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(reqeustOrderTable));

            // when && then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_요리중_이거나_식사_중이면_에외() {
            // given
            OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(true);

            OrderTable reqeustOrderTable = OrderTableFixture.builder()
                .build();
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(reqeustOrderTable));

            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(true);

            // when && then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경_성공() {
            // given
            boolean changeEmpty = true;
            OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(changeEmpty);

            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(OrderTableFixture.builder()
                    .withEmpty(!changeEmpty)
                    .build()));

            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(false);

            given(orderTableRepository.save(any()))
                .willReturn(OrderTableFixture.builder()
                    .withEmpty(changeEmpty)
                    .build());

            // when
            tableService.changeEmpty(orderTableId, request);

            // then
            assertSoftly(softAssertions -> {
                verify(orderTableRepository, times(1)).save(orderTableArgumentCaptor.capture());
                OrderTable captorValue = orderTableArgumentCaptor.getValue();
                assertThat(captorValue.isEmpty()).isEqualTo(changeEmpty);
            });
        }
    }

    @Nested
    class 손님_수_변경 {

        private long orderTableId = 1L;

        @Test
        void 손님_수가_음수면_예외() {
            // given
            OrderTableGuestChangeRequest request = new OrderTableGuestChangeRequest(-1);

            // when && then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_없으면_예외() {
            // given
            OrderTableGuestChangeRequest request = new OrderTableGuestChangeRequest(1);

            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_주문_테이블의_손님수를_바꾸면_예외() {
            // given
            OrderTableGuestChangeRequest request = new OrderTableGuestChangeRequest(1);

            OrderTable requestOrderTable = OrderTableFixture.builder()
                .withNumberOfGuests(1)
                .build();

            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(
                    OrderTableFixture.builder()
                        .withEmpty(true)
                        .build()));

            // when && then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경_성공() {
            // given
            OrderTableGuestChangeRequest request = new OrderTableGuestChangeRequest(1);

            OrderTable requestOrderTable = OrderTableFixture.builder()
                .withNumberOfGuests(1)
                .build();

            OrderTable orderTable = OrderTableFixture.builder()
                .withEmpty(false)
                .build();

            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

            given(orderTableRepository.save(any()))
                .willReturn(orderTable);

            // when
            tableService.changeNumberOfGuests(orderTableId, request);

            // then
            assertSoftly(softAssertions -> {
                verify(orderTableRepository, times(1)).save(orderTableArgumentCaptor.capture());
                OrderTable value = orderTableArgumentCaptor.getValue();
                assertThat(value.getNumberOfGuests()).isEqualTo(1);
            });
        }
    }
}


