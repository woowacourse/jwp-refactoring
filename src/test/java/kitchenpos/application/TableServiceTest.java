package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.application.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.application.dto.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.domain.MenuPrice;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeEmpty_failNotExistOrderTable() {
        // given
        final Long notExistedTableId = 0L;
        given(orderTableRepository.getById(notExistedTableId))
            .willThrow(IllegalArgumentException.class);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(notExistedTableId, new OrderTableUpdateEmptyRequest(true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 COMPLETION 상태가 아니면 예외가 발생한다.")
    @Test
    void changeEmpty_failNotCompletionStatus() {
        // given
        final Long orderTableId = 1L;
        final int numberOfGuests = 2;

        given(orderTableRepository.getById(orderTableId))
            .willReturn(new OrderTable(orderTableId, numberOfGuests, false, Collections.singletonList(
                new Order(1L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null))))));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, new OrderTableUpdateEmptyRequest(true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final Long orderTableId = 1L;
        final int numberOfGuests = 5;

        final int previousNumberOfGuests = 1;
        given(orderTableRepository.getById(orderTableId))
            .willReturn(
                new OrderTable(orderTableId, previousNumberOfGuests, false, Collections.emptyList()));

        // when
        final OrderTableResponse savedOrderTable = tableService.changeNumberOfGuests(orderTableId,
                                                                                     new OrderTableUpdateNumberOfGuestsRequest(
                                                                                         numberOfGuests));

        // then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("손님 수가 음수이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_failNegativeNumberOfGuests() {
        // given
        final Long orderTableId = 1L;
        given(orderTableRepository.getById(orderTableId))
            .willReturn(new OrderTable(orderTableId, 1, false, Collections.emptyList()));

        // when
        // then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(orderTableId, new OrderTableUpdateNumberOfGuestsRequest(-10)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블을 수정하려고 하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_failNotExistOrderTable() {
        // given
        final Long notExistedTableId = 0L;
        given(orderTableRepository.getById(notExistedTableId))
            .willThrow(IllegalArgumentException.class);

        // when
        // then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(notExistedTableId, new OrderTableUpdateNumberOfGuestsRequest(1)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_failEmptyTable() {
        // given
        final Long orderTableId = 1L;
        final int numberOfGuests = 5;

        final int previousNumberOfGuests = 1;
        given(orderTableRepository.getById(orderTableId))
            .willReturn(
                new OrderTable(orderTableId, previousNumberOfGuests, true, Collections.emptyList()));

        // when
        // then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(orderTableId,
                                                    new OrderTableUpdateNumberOfGuestsRequest(numberOfGuests)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
