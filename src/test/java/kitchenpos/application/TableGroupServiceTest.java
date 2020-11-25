package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.exception.NotEnoughTableException;
import kitchenpos.exception.TableGroupCannotChangeException;
import kitchenpos.exception.TableNotExistenceException;
import kitchenpos.fixture.TestFixture;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest extends TestFixture {

    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private TableDao tableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, tableDao, tableGroupDao);
    }

    @DisplayName("테이블 그룹 생성 예외 테스트: 사이즈가 하나 이하일 때")
    @Test
    void createFailByNotEnoughTables() {
        TableGroupCreateRequest notEnoughTableGroupRequest =
            new TableGroupCreateRequest(Arrays.asList(TABLE_ID_1));

        assertThatThrownBy(() -> tableGroupService.create(notEnoughTableGroupRequest))
            .isInstanceOf(NotEnoughTableException.class);
    }

    @DisplayName("테이블 그룹 생성 예외 테스트: 테이블이 중복될 때")
    @Test
    void createFailByDuplicatedTables() {
        TableGroupCreateRequest duplicatedTableGroupRequest =
            new TableGroupCreateRequest(Arrays.asList(TABLE_ID_1, TABLE_ID_1));

        given(tableDao.findAllByIdIn(any())).willReturn(Arrays.asList(TABLE_1));

        assertThatThrownBy(() -> tableGroupService.create(duplicatedTableGroupRequest))
            .isInstanceOf(TableNotExistenceException.class);
    }

    @DisplayName("테이블 그룹 생성 성공 테스트")
    @Test
    void createTest() {
        Table table1 = new Table(TABLE_ID_1, null, TABLE_NUMBER_OF_GUESTS_1, !TABLE_EMPTY_1);
        Table table2 = new Table(TABLE_ID_2, null, TABLE_NUMBER_OF_GUESTS_2, !TABLE_EMPTY_2);

        given(tableDao.findAllByIdIn(any())).willReturn(Arrays.asList(table1, table2));
        given(tableGroupDao.save(any())).willReturn(TABLE_GROUP);

        TableGroupCreateRequest tableGroupCreateRequest =
            new TableGroupCreateRequest(Arrays.asList(TABLE_ID_1, TABLE_ID_2));

        assertThat(tableGroupService.create(tableGroupCreateRequest)).usingRecursiveComparison().isEqualTo(TABLE_GROUP);
    }

    @DisplayName("테이블 그룹 해제 예외 테스트: 아직 식사 중 또는 요리 중일 때")
    @Test
    void ungroupFailByNotCompleted() {
        given(tableDao.findAllByTableGroupId(anyLong())).willReturn(TABLES);
        given(orderDao.findByTableIds(any())).willReturn(Arrays.asList(ORDER_1));

        assertThatThrownBy(() -> tableGroupService.ungroup(anyLong()))
            .isInstanceOf(TableGroupCannotChangeException.class);
    }

    @DisplayName("테이블 그룹 해제 성공 테스트")
    @Test
    void ungroupTest() {
        Table table1 = new Table(TABLE_ID_1, TABLE_GROUP_ID, 0, TABLE_EMPTY_1);
        Table table2 = new Table(TABLE_ID_2, TABLE_GROUP_ID, 0, TABLE_EMPTY_2);

        given(tableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(table1, table2));
        given(orderDao.findByTableIds(any()))
            .willReturn(Arrays.asList(new Order(ORDER_ID_1, TABLE_ID_1, OrderStatus.COMPLETION, LocalDateTime.now())));

        tableGroupService.ungroup(TABLE_GROUP_ID);
        assertAll(
            () -> assertThat(table1.getTableGroupId()).isNull(),
            () -> assertThat(table2.getTableGroupId()).isNull(),
            () -> assertThat(table1.isEmpty()).isTrue(),
            () -> assertThat(table2.isEmpty()).isTrue()
        );
    }
}