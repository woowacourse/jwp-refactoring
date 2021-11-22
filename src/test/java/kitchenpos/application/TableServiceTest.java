package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static kitchenpos.testutils.TestDomainBuilder.orderTableBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@MockitoSettings
class TableServiceTest {

    private static final Long NON_EXISTENT_ID = 987654321L;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable newOrderTable = orderTableBuilder()
                .numberOfGuests(0)
                .empty(true)
                .build();

        // when
        tableService.create(newOrderTable);

        // then
        then(orderTableDao).should(times(1)).save(newOrderTable);
    }

    @DisplayName("전체 테이블의 리스트를 가져온다.")
    @Test
    void list() {
        // when
        tableService.list();

        // then
        then(orderTableDao).should(times(1)).findAll();
    }

    @DisplayName("테이블의 비어 있음 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        Long orderTableId = 1L;
        OrderTable nonEmptyTable = orderTableBuilder()
                .empty(false)
                .build();

        OrderTable savedOrderTable = orderTableBuilder()
                .id(orderTableId)
                .tableGroupId(null)
                .empty(true)
                .build();

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.ofNullable(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(eq(orderTableId), anyList())).willReturn(false);
        given(orderTableDao.save(savedOrderTable)).willReturn(savedOrderTable);

        // when
        OrderTable orderTable = tableService.changeEmpty(orderTableId, nonEmptyTable);

        // then
        assertThat(orderTable.isEmpty()).isFalse();

        then(orderTableDao).should(times(1)).findById(orderTableId);
        then(orderDao).should(times(1))
                .existsByOrderTableIdAndOrderStatusIn(eq(orderTableId), anyList());
        then(orderTableDao).should(times(1)).save(savedOrderTable);
    }

    @DisplayName("테이블의 비어 있음 여부 변경에 실패한다 - 존재하지 않는 테이블")
    @Test
    void changeEmptyWhenNonExistentTable() {
        // given
        OrderTable nonEmptyTable = orderTableBuilder()
                .empty(false)
                .build();

        given(orderTableDao.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(NON_EXISTENT_ID, nonEmptyTable))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블의 비어 있음 여부 변경에 실패한다 - 단체 지정이 되어있는 테이블")
    @Test
    void changeEmptyWhenGroupTable() {
        // given
        Long orderTableId = 1L;
        OrderTable nonEmptyTable = orderTableBuilder()
                .empty(false)
                .build();

        OrderTable savedOrderTable = orderTableBuilder()
                .id(orderTableId)
                .tableGroupId(1L)
                .empty(true)
                .build();

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.ofNullable(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, nonEmptyTable))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블의 비어 있음 여부 변경에 실패한다 - 완료가 아닌 조리 또는 식사 상태의 주문이 있는 경우")
    @Test
    void changeEmptyWhenExistentNotCompletedOrder() {
        // given
        Long orderTableId = 1L;
        OrderTable nonEmptyTable = orderTableBuilder()
                .empty(false)
                .build();

        OrderTable savedOrderTable = orderTableBuilder()
                .id(orderTableId)
                .tableGroupId(null)
                .empty(true)
                .build();

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.ofNullable(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(eq(orderTableId), anyList())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, nonEmptyTable))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        Long orderTableId = 1L;
        OrderTable expectedNumberTable = orderTableBuilder()
                .numberOfGuests(4)
                .build();

        OrderTable savedOrderTable = orderTableBuilder()
                .id(orderTableId)
                .empty(false)
                .build();

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.ofNullable(savedOrderTable));
        given(orderTableDao.save(savedOrderTable)).willReturn(savedOrderTable);

        // when
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, expectedNumberTable);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);

        then(orderTableDao).should(times(1)).findById(orderTableId);
        then(orderTableDao).should(times(1)).save(savedOrderTable);
    }

    @DisplayName("테이블의 방문한 손님 수를 변경에 실패한다 - 변경할 손님 수가 음수")
    @Test
    void changeNumberOfGuestsWhenNegativeNumber() {
        // given
        Long orderTableId = 1L;
        OrderTable expectedNumberTable = orderTableBuilder()
                .numberOfGuests(-1)
                .build();

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, expectedNumberTable))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블의 방문한 손님 수를 변경에 실패한다 - 존재하지 않는 테이블")
    @Test
    void changeNumberOfGuestsWhenNonExistentTable() {
        // given
        OrderTable expectedNumberTable = orderTableBuilder()
                .numberOfGuests(4)
                .build();

        given(orderTableDao.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(NON_EXISTENT_ID, expectedNumberTable))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블의 방문한 손님 수를 변경에 실패한다 - 빈 테이블")
    @Test
    void changeNumberOfGuestsWhenEmptyTable() {
        // given
        Long orderTableId = 1L;
        OrderTable expectedNumberTable = orderTableBuilder()
                .numberOfGuests(4)
                .build();

        OrderTable savedOrderTable = orderTableBuilder()
                .id(orderTableId)
                .empty(true)
                .build();

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.ofNullable(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, expectedNumberTable))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderTableDao).should(never()).save(any());
    }
}
