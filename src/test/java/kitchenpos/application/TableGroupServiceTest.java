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

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    private List<OrderTable> tables;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao,
            orderTableRepository);

        tables = Lists.newArrayList(OrderTableFixture.createEmptyWithId(OrderTableFixture.ID1),
            OrderTableFixture.createEmptyWithId(OrderTableFixture.ID2));
    }

    @DisplayName("정상적으로 테이블을 그룹화 한다.")
    @Test
    void create() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        TableGroup tableWithId = TableGroupFixture.createWithId(1L);

        when(orderTableRepository.findAllByTableGroupIdIn(anyList())).thenReturn(tables);
        when(tableGroupDao.save(any())).thenReturn(tableWithId);

        TableGroup savedTableGroup = tableGroupService.create(request);

        assertThat(savedTableGroup).isEqualToComparingFieldByField(tableWithId);
    }

    @DisplayName("그룹 요청 테이블의 개수가 0개인 경우 예외를 반환한다.")
    @Test
    void createWithEmptyTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("그룹 요청 테이블의 개수가 1개인 경우 예외를 반환한다.")
    @Test
    void createWithOneTable() {
        TableGroupCreateRequest request = TableGroupFixture.createOneTableRequest();

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실제 존재하지 않는 테이블을 그룹화하는 경우 예외를 반환한다.")
    @Test
    void createWithNotExistTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty가 아닌 상태의 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotEmptyTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        TableGroup containsEmptyTable = TableGroupFixture.createWithoutId();
        tables.add(OrderTableFixture.createNotEmptyWithId(OrderTableFixture.ID3));

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 다른 그룹이 있는 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotNullGroupTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        tables.add(OrderTableFixture.createGroupTableWithId(3L, 1L));

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 테이블을 언그룹화 한다.")
    @Test
    void ungroup() {
        TableGroup withId = TableGroupFixture.createWithId(1L);
        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(tables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
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
        when(orderTableDao.findAllByTableGroupId(1L))
            .thenReturn(Arrays.asList(tables.get(0), tables.get(1)));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(
            true);

        assertThatThrownBy(() -> tableGroupService.ungroup(withNotCompleteTable.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
