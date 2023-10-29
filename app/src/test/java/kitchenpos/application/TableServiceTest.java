package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.common.event.OrderCheckEvent;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.TableCreateRequest;
import kitchenpos.table.dto.request.TableUpdateEmptyRequest;
import kitchenpos.table.dto.request.TableUpdateGuestRequest;
import kitchenpos.table.dto.response.TableResponse;
import kitchenpos.table.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private TableService tableService;

    @DisplayName("새로운 테이블을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(2, true);

        final OrderTable orderTable = new OrderTable(tableGroup.getId(), 2, true);
        given(orderTableRepository.save(any()))
                .willReturn(orderTable);

        // when & then
        assertThat(tableService.create(tableCreateRequest)).isEqualTo(orderTable.getId());
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final OrderTable orderTable1 = new OrderTable(tableGroup.getId(), 2, true);
        final OrderTable orderTable2 = new OrderTable(tableGroup.getId(), 3, true);
        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        final List<TableResponse> responses = orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toUnmodifiableList());

        given(orderTableRepository.findAll())
                .willReturn(orderTables);

        // when & then
        assertThat(tableService.list()).usingRecursiveComparison().isEqualTo(responses);
    }

    @DisplayName("테이블의 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final TableUpdateEmptyRequest updateRequest = new TableUpdateEmptyRequest(true);
        final OrderTable orderTable = new OrderTable(1L, null, 2, true);

        final TableResponse tableResponse = TableResponse.from(orderTable);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));
        doNothing().when(publisher).publishEvent(any(OrderCheckEvent.class));
        given(orderTableRepository.save(orderTable))
                .willReturn(orderTable);

        // when & then
        assertThat(tableService.changeEmpty(1L, updateRequest)).usingRecursiveComparison().isEqualTo(tableResponse);
        then(orderTableRepository).should(times(1)).findById(anyLong());
        then(orderTableRepository).should(times(1)).save(any());
    }

    @DisplayName("주문 테이블이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeEmpty_FailWhenTableIsEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final TableUpdateEmptyRequest updateRequest = new TableUpdateEmptyRequest(true);
        final OrderTable orderTable = new OrderTable(1L, tableGroup.getId(), 2, true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("테이블에 할당된 그룹이 존재하면 변경할 수 없다.")
    @Test
    void changeEmpty_FailWhenGroupAlreadyAssigned() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final TableUpdateEmptyRequest updateRequest = new TableUpdateEmptyRequest(true);
        final OrderTable orderTable = new OrderTable(1L, tableGroup.getId(), 2, true);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("할당된 그룹이 존재합니다.");
    }

    @DisplayName("테이블에 할당된 손님의 수를 조정할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);

        final TableUpdateGuestRequest updateRequest = new TableUpdateGuestRequest(8);
        final OrderTable beforeTable = new OrderTable(1L, tableGroup.getId(), 1, false);
        final OrderTable afterTable = new OrderTable(1L, tableGroup.getId(), 8, false);

        final TableResponse tableResponse = TableResponse.from(afterTable);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(beforeTable));

        given(orderTableRepository.save(any()))
                .willReturn(afterTable);

        // when & then
        assertThat(tableService.changeNumberOfGuests(1L, updateRequest)).usingRecursiveComparison()
                .isEqualTo(tableResponse);
        then(orderTableRepository).should(times(1)).findById(1L);
        then(orderTableRepository).should(times(1)).save(any());
    }

    @DisplayName("존재하지 않는 주문 테이블이면 조정할 수 없다.")
    @Test
    void changeNumberOfGuests_FailWhenOrderTableNotExist() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);

        final TableUpdateGuestRequest updateRequest = new TableUpdateGuestRequest(8);
        final OrderTable orderTable = new OrderTable(1L, tableGroup.getId(), 5, true);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @DisplayName("테이블의 상태가 비어있으면 조정할 수 없다.")
    @Test
    void changeNumberOfGuests_FailWhenTableIsEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);

        final TableUpdateGuestRequest updateRequest = new TableUpdateGuestRequest(8);
        final OrderTable beforeTable = new OrderTable(1L, tableGroup.getId(), 1, false);
        final OrderTable afterTable = new OrderTable(1L, tableGroup.getId(), 8, true);

        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(afterTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 상태가 비어 있습니다.");
    }
}
