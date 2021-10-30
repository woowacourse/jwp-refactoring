package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.request.table.CreateTableGroupRequest;
import kitchenpos.dto.request.table.TableIdRequest;
import kitchenpos.dto.response.table.TableGroupResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.fixture.OrderFixture.COMPLETION_ORDER;
import static kitchenpos.fixture.OrderFixture.COOKING_ORDER;
import static kitchenpos.fixture.OrderTableFixture.단일_손님0_테이블1;
import static kitchenpos.fixture.OrderTableFixture.단일_손님0_테이블2;
import static kitchenpos.fixture.TableGroupFixture.GROUP1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("TableGroupService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블을 묶어 그룹을 지정할 수 있다 - 그룹이 지정된 테이블들은 비어 있지 않은 상태가 된다.")
    void create() {
        // given
        CreateTableGroupRequest group = new CreateTableGroupRequest(
                Arrays.asList(new TableIdRequest(단일_손님0_테이블1.getId()), new TableIdRequest(단일_손님0_테이블2.getId()))
        );

        TableGroup expected = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(단일_손님0_테이블1, 단일_손님0_테이블2));

        given(orderTableRepository.findById(단일_손님0_테이블1.getId())).willReturn(Optional.of(단일_손님0_테이블1));
        given(orderTableRepository.findById(단일_손님0_테이블2.getId())).willReturn(Optional.of(단일_손님0_테이블2));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(expected);

        // when
        TableGroupResponse actual = tableGroupService.create(group);

        // then
        assertEquals(2, actual.getOrderTables().size());
        assertFalse(actual.getOrderTables().get(0).isEmpty());
        assertFalse(actual.getOrderTables().get(1).isEmpty());
        assertNotNull(actual.getCreatedDate());
        assertThat(actual).usingRecursiveComparison().ignoringFields("createdDate").isEqualTo(expected);
    }

    @Test
    @DisplayName("목록에 둘 이상의 테이블이 포함되어야한다.")
    void createWrongTableInsufficientTable() {
        // given
        OrderTable table = new OrderTable(1L, null, 0, false);
        CreateTableGroupRequest group = new CreateTableGroupRequest(Collections.singletonList(new TableIdRequest(table.getId())));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(table));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(group));
        assertEquals("그룹을 지정하려면 둘 이상의 테이블이 필요합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("목록에 포함된 테이블들은 모두 등록된 테이블이여야 한다.")
    void createWrongTableNotRegister() {
        // given
        CreateTableGroupRequest group = new CreateTableGroupRequest(
                Arrays.asList(new TableIdRequest(단일_손님0_테이블1.getId()), new TableIdRequest(단일_손님0_테이블2.getId()))
        );

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(group));
        assertEquals("등록되지 않은 테이블은 그룹으로 지정할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("목록에 포함된 테이블들은 모두 비어있어야 한다.")
    void createWrongTableNotEmpty() {
        // given
        CreateTableGroupRequest group = new CreateTableGroupRequest(
                Arrays.asList(new TableIdRequest(단일_손님0_테이블1.getId()), new TableIdRequest(단일_손님0_테이블2.getId()))
        );

        OrderTable table1 = new OrderTable(단일_손님0_테이블1.getId(), null, 3, false);
        OrderTable table2 = new OrderTable(단일_손님0_테이블2.getId(), null, 5, true);

        given(orderTableRepository.findById(table1.getId())).willReturn(Optional.of(table1));
        given(orderTableRepository.findById(table2.getId())).willReturn(Optional.of(table2));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(group));
        assertEquals("테이블이 비어있지 않거나 이미 다른 그룹에 속한 테이블은 그룹으로 지정할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("목록에 포함된 테이블들은 모두 소속된 다른 그룹이 없어야한다.")
    void createWrongTableInGroup() {
        // given
        CreateTableGroupRequest group = new CreateTableGroupRequest(
                Arrays.asList(new TableIdRequest(단일_손님0_테이블1.getId()), new TableIdRequest(단일_손님0_테이블2.getId()))
        );

        OrderTable table1 = new OrderTable(단일_손님0_테이블1.getId(), null, 3, true);
        OrderTable table2 = new OrderTable(단일_손님0_테이블2.getId(), GROUP1, 5, true);

        given(orderTableRepository.findById(table1.getId())).willReturn(Optional.of(table1));
        given(orderTableRepository.findById(table2.getId())).willReturn(Optional.of(table2));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(group));
        assertEquals("테이블이 비어있지 않거나 이미 다른 그룹에 속한 테이블은 그룹으로 지정할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("묶여있는 테이블 그룹을 해제할 수 있다. - 그룹이 해제된 테이블들은 비어 있지 않은 상태가 된다.")
    void ungroup() {
        // given
        OrderTable table1 = new OrderTable(1L, null, 0, true, Collections.singletonList(COMPLETION_ORDER));
        OrderTable table2 = new OrderTable(2L, null, 0, true, Collections.singletonList(COMPLETION_ORDER));
        TableGroup GROUP2 = new TableGroup(
                2L,
                LocalDateTime.now(),
                Arrays.asList(table1, table2));
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(GROUP2));

        // when
        tableGroupService.ungroup(GROUP2.getId());

        // then
        assertNull(table1.getTableGroup());
        assertNull(table2.getTableGroup());
        assertFalse(table1.isEmpty());
        assertFalse(table2.isEmpty());
    }

    @Test
    @DisplayName("목록에 포함된 테이블들의 상태가 하나라도 조리중(COOKING)이나 식사중(MEAL)인 경우 그룹을 해제할 수 없다.")
    void ungroupWrongTableCookingOrMeal() {
        // given
        TableGroup GROUP2 = new TableGroup(
                2L,
                LocalDateTime.now(),
                Arrays.asList(
                        new OrderTable(1L, null, 0, true, Collections.singletonList(COOKING_ORDER)),
                        new OrderTable(2L, null, 0, true, Collections.singletonList(COMPLETION_ORDER))
                )
        );

        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(GROUP2));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.ungroup(GROUP2.getId()));
        assertEquals("주문 상태가 조리중이나 식사중입니다.", exception.getMessage());
    }
}
