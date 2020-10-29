package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.fixture.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest extends TestFixture {

    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("테이블 그룹 생성 예외 테스트: 사이즈가 하나 이하일 때")
    @Test
    void createFailByNotEnoughTables() {
        TableGroupCreateRequest notEnoughTableGroupRequest =
            new TableGroupCreateRequest(Arrays.asList(ORDER_TABLE_ID_1));

        assertThatThrownBy(() -> tableGroupService.create(notEnoughTableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 예외 테스트: 테이블이 중복될 때")
    @Test
    void createFailByDuplicatedTables() {
        TableGroupCreateRequest duplicatedTableGroupRequest =
            new TableGroupCreateRequest(Arrays.asList(ORDER_TABLE_ID_1, ORDER_TABLE_ID_1));

        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(ORDER_TABLE_1));

        assertThatThrownBy(() -> tableGroupService.create(duplicatedTableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 성공 테스트")
    @Test
    void createTest() {
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        orderTable1.setId(ORDER_TABLE_ID_1);
        orderTable1.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_1);
        orderTable1.setEmpty(!ORDER_TABLE_EMPTY_1);
        orderTable2.setId(ORDER_TABLE_ID_2);
        orderTable2.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_2);
        orderTable2.setEmpty(!ORDER_TABLE_EMPTY_2);

        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(tableGroupDao.save(any())).willReturn(TABLE_GROUP);

        TableGroupCreateRequest tableGroupCreateRequest =
            new TableGroupCreateRequest(Arrays.asList(ORDER_TABLE_ID_1, ORDER_TABLE_ID_2));

        assertThat(tableGroupService.create(tableGroupCreateRequest)).usingRecursiveComparison().isEqualTo(TABLE_GROUP);
    }

    @DisplayName("테이블 그룹 해제 예외 테스트: 아직 식사 중 또는 요리 중일 때")
    @Test
    void ungroupFailByNotCompleted() {
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(ORDER_TABLES);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(anyLong())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제 성공 테스트")
    @Test
    void ungroupTest() {
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        orderTable1.setId(ORDER_TABLE_ID_1);
        orderTable1.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_1);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(TABLE_GROUP_ID);
        orderTable2.setId(ORDER_TABLE_ID_2);
        orderTable2.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_2);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(TABLE_GROUP_ID);

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        tableGroupService.ungroup(TABLE_GROUP_ID);
        assertAll(
            () -> assertThat(orderTable1.getTableGroupId()).isNull(),
            () -> assertThat(orderTable2.getTableGroupId()).isNull(),
            () -> assertThat(orderTable1.isEmpty()).isFalse(),
            () -> assertThat(orderTable2.isEmpty()).isFalse()
        );
    }
}