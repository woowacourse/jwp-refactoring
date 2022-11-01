package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableForGroupingRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @MockBean
    private OrderRepository orderRepository;

    private Long emptyOrderTableId1;
    private Long emptyOrderTableId2;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
        final OrderTable orderTable1 = this.orderTableRepository.save(createOrderTable(0, true));
        final OrderTable orderTable2 = this.orderTableRepository.save(createOrderTable(0, true));
        emptyOrderTableId1 = orderTable1.getId();
        emptyOrderTableId2 = orderTable2.getId();
    }

    @DisplayName("단체 지정을 진행한다.")
    @Test
    void create() {
        // given
        final TableGroupRequest tableGroupRequest = createTableGroupRequest(
                List.of(createOrderTableRequest(emptyOrderTableId1), createOrderTableRequest(emptyOrderTableId2)));

        // when
        final TableGroupResponse response = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("2개 미만의 테이블을 단체 지정하면 예외를 반환한다.")
    @Test
    void create_throwException_ifOrderTableSizeUnderTwo() {
        // given
        final TableGroupRequest tableGroupRequest = createTableGroupRequest(
                List.of(createOrderTableRequest(emptyOrderTableId1)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class).
                hasMessage("2개 이상의 테이블만 단체 지정이 가능합니다.");
    }

    @DisplayName("존재하지 않는 테이블을 단체 지정하면 예외를 반환한다.")
    @Test
    void create_throwException_ifTableNotExist() {
        // given
        final Long invalidTableId = 999L;
        final TableGroupRequest tableGroupRequest = createTableGroupRequest(
                List.of(createOrderTableRequest(emptyOrderTableId1), createOrderTableRequest(invalidTableId)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class).
                hasMessage("존재하지 않는 테이블은 단체 지정할 수 없습니다.");
    }

    @DisplayName("이미 단체 지정된 테이블을 단체 지정하면 예외를 반환한다.")
    @Test
    void create_throwException_ifTableAlreadyGroup() {
        // given
        final TableGroup savedTableGroup = tableGroupRepository.save(createTableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(createOrderTable(savedTableGroup.getId(), 0, true));
        final TableGroupRequest tableGroupRequest = createTableGroupRequest(
                List.of(createOrderTableRequest(orderTable.getId()), createOrderTableRequest(emptyOrderTableId1)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class).
                hasMessage("단체 테이블입니다.");
    }

    @DisplayName("이미 사용 중인 테이블을 단체 지정하면 예외를 반환한다.")
    @Test
    void create_throwException_ifTableNotEmpty() {
        // given
        final OrderTable orderTable = orderTableRepository.save(createOrderTable(4, false));
        final TableGroupRequest tableGroupRequest = createTableGroupRequest(
                List.of(createOrderTableRequest(orderTable.getId()), createOrderTableRequest(emptyOrderTableId1)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class).
                hasMessage("이미 사용 중인 테이블은 사용할 수 없습니다.");
    }

    @DisplayName("테이블 단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        final TableGroup savedTableGroup = tableGroupRepository.save(createTableGroup(LocalDateTime.now()));

        // when, then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("이미 주문이 진행 중인 상태에서 단체 지정을 제거하면 예외를 반환한다.")
    @Test
    void ungroup_throwException_ifOrderAlreadyOngoing() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);
        final TableGroup savedTableGroup = tableGroupRepository.save(createTableGroup(LocalDateTime.now()));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("식사가 완료되지 않았습니다.");
    }

    private TableGroupRequest createTableGroupRequest(final List<TableForGroupingRequest> orderTableRequests) {
        return new TableGroupRequest(orderTableRequests);
    }

    private TableForGroupingRequest createOrderTableRequest(final Long id) {
        return new TableForGroupingRequest(id);
    }
}
