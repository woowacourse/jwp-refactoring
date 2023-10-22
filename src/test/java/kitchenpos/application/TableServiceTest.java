package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("새로운 테이블을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final OrderTable orderTable = new OrderTable(tableGroup, 2, true);
        given(orderTableRepository.save(orderTable))
                .willReturn(orderTable);

        // when & then
        assertThat(tableService.create(orderTable)).isEqualTo(orderTable);
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final OrderTable orderTable1 = new OrderTable(tableGroup, 2, true);
        final OrderTable orderTable2 = new OrderTable(tableGroup, 3, true);
        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        given(orderTableRepository.findAll())
                .willReturn(orderTables);

        // when & then
        assertThat(tableService.list()).isEqualTo(orderTables);
    }

    @DisplayName("테이블의 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final OrderTable orderTable = new OrderTable(1L, null, 2, true);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(orderTable))
                .willReturn(orderTable);

        // when & then
        assertThat(tableService.changeEmpty(1L, orderTable)).isEqualTo(orderTable);
        then(orderTableRepository).should(times(1)).findById(anyLong());
        then(orderTableRepository).should(times(1)).save(any());
    }

    @DisplayName("주문 테이블이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeEmpty_FailWhenTableIsEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final OrderTable orderTable = new OrderTable(1L, tableGroup, 2, true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("테이블에 할당된 그룹이 존재하면 변경할 수 없다.")
    @Test
    void changeEmpty_FailWhenGroupAlreadyAssigned() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);

        final OrderTable orderTable = new OrderTable(1L, tableGroup, 2, true);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("할당된 그룹이 존재합니다.");
    }

    @DisplayName("테이블에 할당된 주문의 상태가 조리 또는 식사이면 변경할 수 없다.")
    @Test
    void changeEmpty_FailWhenOrderStatusIsNotCompletion() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 3, true);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));

        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 아직 완료상태가 아닙니다.");
    }

    @DisplayName("테이블에 할당된 손님의 수를 조정할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);

        final OrderTable beforeTable = new OrderTable(1L, tableGroup, 1, false);
        final OrderTable afterTable = new OrderTable(1L, tableGroup, 8, false);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(beforeTable));

        given(orderTableRepository.save(any()))
                .willReturn(afterTable);

        // when & then
        assertThat(tableService.changeNumberOfGuests(1L, afterTable)).isEqualTo(afterTable);
        then(orderTableRepository).should(times(1)).findById(1L);
        then(orderTableRepository).should(times(1)).save(any());
    }

    @DisplayName("테이블에 할당된 손님의 수가 0 미만이면 조정할 수 없다.")
    @Test
    void changeNumberOfGuests_FailWhenChangeGuestNumUnderZero() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);

        final OrderTable orderTable = new OrderTable(1L, tableGroup, -1, false);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조정하려는 손님의 수가 0 미만입니다.");
    }

    @DisplayName("존재하지 않는 주문 테이블이면 조정할 수 없다.")
    @Test
    void changeNumberOfGuests_FailWhenOrderTableNotExist() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);

        final OrderTable orderTable = new OrderTable(1L, tableGroup, 5, true);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @DisplayName("테이블의 상태가 비어있으면 조정할 수 없다.")
    @Test
    void changeNumberOfGuests_FailWhenTableIsEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);

        final OrderTable beforeTable = new OrderTable(1L, tableGroup, 1, false);
        final OrderTable afterTable = new OrderTable(1L, tableGroup, 8, true);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(afterTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, beforeTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 상태가 비어 있습니다.");
    }
}
