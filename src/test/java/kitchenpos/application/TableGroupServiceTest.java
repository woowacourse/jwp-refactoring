package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.exception.AlreadyInTableGroupException;
import kitchenpos.exception.OrderNotCompleteException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.TableGroupSizeException;
import kitchenpos.exception.TableGroupWithNotEmptyTableException;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private TableGroupService tableGroupService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    private List<OrderTable> tables;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository,
            tableGroupRepository);

        tables = Lists.newArrayList(OrderTableFixture.createEmptyWithId(OrderTableFixture.ID1),
            OrderTableFixture.createEmptyWithId(OrderTableFixture.ID2));
    }

    @DisplayName("정상적으로 테이블을 그룹화 한다.")
    @Test
    void create() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        TableGroup tableWithId = TableGroupFixture.createWithId(1L);

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(tables);
        when(tableGroupRepository.save(any())).thenReturn(tableWithId);

        TableGroup savedTableGroup = tableGroupService.create(request);

        assertThat(savedTableGroup).isEqualToComparingFieldByField(tableWithId);
    }

    @DisplayName("그룹 요청 테이블의 개수가 0개인 경우 예외를 반환한다.")
    @Test
    void createWithEmptyTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(OrderTableNotFoundException.class)
            .hasMessage("Table of table group request is not exist");

    }

    @DisplayName("그룹 요청 테이블의 개수가 1개인 경우 예외를 반환한다.")
    @Test
    void createWithOneTable() {
        TableGroupCreateRequest request = TableGroupFixture.createOneTableRequest();

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(TableGroupSizeException.class)
            .hasMessage(String.format("%d size table group request is invalid",
                request.getOrderTables().size()));
    }

    @DisplayName("실제 존재하지 않는 테이블을 그룹화하는 경우 예외를 반환한다.")
    @Test
    void createWithNotExistTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        tables.remove(0);

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(OrderTableNotFoundException.class)
            .hasMessage("Table of table group request is not exist");
    }

    @DisplayName("Empty가 아닌 상태의 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotEmptyTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        TableGroup withId = TableGroupFixture.createWithId(1L);
        tables.remove(1);
        tables.add(OrderTableFixture.createNotEmptyWithId(OrderTableFixture.ID3));

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(tables);
        when(tableGroupRepository.save(any())).thenReturn(withId);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(TableGroupWithNotEmptyTableException.class)
            .hasMessage(
                String.format("%d table is not empty Table. For group tables, first change empty",
                    OrderTableFixture.ID3));
    }

    @DisplayName("이미 다른 그룹이 있는 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotNullGroupTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        TableGroup withId = TableGroupFixture.createWithId(1L);
        tables.remove(1);
        tables.add(OrderTableFixture.createGroupTableWithId(3L, 1L));

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(tables);
        when(tableGroupRepository.save(any())).thenReturn(withId);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(AlreadyInTableGroupException.class)
            .hasMessage(String.format("%d table is already in table group %d", 3L, 1L));
    }

    @DisplayName("정상적으로 테이블을 언그룹화 한다.")
    @Test
    void ungroup() {
        TableGroup withId = TableGroupFixture.createWithId(1L);
        when(orderTableRepository.findAllByTableGroupId(anyLong())).thenReturn(tables);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .thenReturn(false);

        tableGroupService.ungroup(withId.getId());
        assertThat(tables)
            .extracting(OrderTable::getTableGroupId)
            .allMatch(Objects::isNull);
    }

    @DisplayName("식사를 마치치 않은 상태(Meal, Cooking)인 테이블을 Group 해제할 때 예외를 반환한다.")
    @Test
    void ungroupWithNotCompleteTable() {
        TableGroup withNotCompleteTable = TableGroupFixture.createWithId(1L);
        when(orderTableRepository.findAllByTableGroupId(1L))
            .thenReturn(Arrays.asList(tables.get(0), tables.get(1)));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(withNotCompleteTable.getId()))
            .isInstanceOf(OrderNotCompleteException.class)
            .hasMessage("Order is not completion yet. For ungroup table, first complete order");
    }
}
