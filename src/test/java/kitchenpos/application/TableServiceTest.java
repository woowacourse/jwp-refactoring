package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.FixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create_orderTable() {
        // given
        final OrderTable forSaveOrderTable = FixtureFactory.forSaveOrderTable(10, false);

        final OrderTable savedOrderTable = FixtureFactory.saveOrderTable(1L, null, 10, false);

        given(orderTableDao.save(any()))
                .willReturn(savedOrderTable);

        // when
        final OrderTable result = tableService.create(forSaveOrderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(savedOrderTable.getId());
            softly.assertThat(result.getTableGroupId()).isEqualTo(savedOrderTable.getTableGroupId());
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests());
        });
    }

    @DisplayName("전체 주문 테이블을 조회한다.")
    @Test
    void find_all_orderTable() {
        // given
        final OrderTable orderTable1 = FixtureFactory.saveOrderTable(1L, 1L, 10, false);

        final OrderTable orderTable2 = FixtureFactory.saveOrderTable(2L, 2L, 10, false);

        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        given(orderTableDao.findAll())
                .willReturn(orderTables);

        // when
        final List<OrderTable> result = tableService.list();

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(orderTables);
    }

    @DisplayName("주문 테이블의 상태를 변경할 수 있다.")
    @Test
    void change_orderTable_empty() {
        // given
        final OrderTable orderTable = FixtureFactory.saveOrderTable(1L, null, 10, false);

        final OrderTable changeOrderTable = FixtureFactory.saveOrderTable(1L, 1L, 10, true);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(orderTable));

        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .willReturn(false);

        given(orderTableDao.save(orderTable))
                .willReturn(changeOrderTable);

        // when
        final OrderTable result = tableService.changeEmpty(orderTable.getId(), changeOrderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(changeOrderTable.getId());
            softly.assertThat(result.getTableGroupId()).isEqualTo(changeOrderTable.getTableGroupId());
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(changeOrderTable.getNumberOfGuests());
        });
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문 테이블의 상태를 변경할 수 없다.")
    @Test
    void change_orderTable_empty_fail_with_not_exist_orderTable() {
        // given
        final Long wrongOrderTableId = 0L;

        final OrderTable changeOrderTable = FixtureFactory.saveOrderTable(1L, 1L, 10, true);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(wrongOrderTableId, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 테이블 그룹이 존재하면 주문 테이블의 상태를 변경할 수 없다.")
    @Test
    void change_orderTable_empty_fail_with_not_null_group() {
        // given
        final OrderTable orderTable = FixtureFactory.saveOrderTable(1L, 1L, 10, false);

        final OrderTable changeOrderTable = FixtureFactory.saveOrderTable(1L, 1L, 10, true);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(orderTable));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 COOKING이거나 MEAL인 경우 주문 테이블의 상태를 변경할 수 없다.")
    @Test
    void change_orderTable_empty_fail_with_invalid_orderStatus() {
        // given
        final OrderTable orderTable = FixtureFactory.saveOrderTable(1L, null, 10, false);

        final OrderTable changeOrderTable = FixtureFactory.saveOrderTable(1L, 1L, 10, true);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(orderTable));

        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void change_number_of_guests() {
        // given
        final OrderTable orderTable = FixtureFactory.saveOrderTable(1L, 1L, 1, false);

        final OrderTable changeOrderTable = FixtureFactory.saveOrderTable(1L, 1L, 10, false);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(orderTable));

        given(orderTableDao.save(any()))
                .willReturn(changeOrderTable);

        // when
        final OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(changeOrderTable.getId());
            softly.assertThat(result.getTableGroupId()).isEqualTo(changeOrderTable.getTableGroupId());
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(changeOrderTable.getNumberOfGuests());
        });
    }

    @DisplayName("변경할 주문 테이블의 손님 수가 음수이면 손님 수를 변경할 수 없다.")
    @Test
    void change_number_of_guests_fail_with_number_of_guests_is_negative() {
        // given
        final Long orderTableId = 1L;

        final OrderTable changeOrderTable = FixtureFactory.saveOrderTable(1L, 1L, -1, false);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않는 경우 손님 수를 변경할 수 없다.")
    @Test
    void change_number_of_guests_fail_with_not_found_orderTable() {
        // given
        final Long wrongOrderTableId = 0L;

        final OrderTable changeOrderTable = FixtureFactory.saveOrderTable(1L, 1L, 10, false);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(wrongOrderTableId, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 손님 수를 변경할 수 없다.")
    @Test
    void change_number_of_guests_fail_with_empty_orderTable() {
        // given
        final OrderTable orderTable = FixtureFactory.saveOrderTable(1L, 1L, 1, true);

        final OrderTable changeOrderTable = FixtureFactory.saveOrderTable(1L, 1L, 3, false);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(orderTable));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
