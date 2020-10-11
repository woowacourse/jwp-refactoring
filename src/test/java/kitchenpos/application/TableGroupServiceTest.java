package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private TableGroupService tableGroupService;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("주문 테이블이 비어있거나 1개일 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsEmptyOrOne() {
        OrderTable orderTable = createOrderTable(1L, true, null, 3);
        TableGroup tableGroup = createTableGroup(1L, LocalDateTime.now(), Collections.emptyList());
        TableGroup tableGroup2 = createTableGroup(1L, LocalDateTime.now(), Collections.singletonList(orderTable));

        assertAll(
            () -> assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class),

            () -> assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("주문 테이블이 중복되거나 없을 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsNullOrDuplicated() {
        OrderTable orderTable = createOrderTable(1L, true, null, 3);
        TableGroup tableGroup = createTableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, orderTable));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(orderTable));
        assertThatThrownBy(
            () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인자로 넘겨준 테이블그룹의 주문 테이블이 비어있거나 없을 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsNullOrDuplicated2() {
        OrderTable orderTableWhichIsEmpty = createOrderTable(1L, true, null, 3);
        TableGroup tableGroupWithTableWhichIsEmpty = createTableGroup(1L, LocalDateTime.now(),
            Arrays.asList(orderTableWhichIsEmpty));
        OrderTable orderTableWithNoGroupId = createOrderTable(1L, true, null, 3);
        TableGroup tableGroupWithTableWithoutTableGroupId = createTableGroup(1L, LocalDateTime.now(),
            Arrays.asList(orderTableWithNoGroupId));
        assertAll(
            () -> assertThatThrownBy(
                () -> tableGroupService.create(tableGroupWithTableWhichIsEmpty)
            ).isInstanceOf(IllegalArgumentException.class),

            () -> assertThatThrownBy(
                () -> tableGroupService.create(tableGroupWithTableWithoutTableGroupId)
            ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("단체 지정을 한다.")
    @Test
    void create() {
        OrderTable orderTable = createOrderTable(1L, true, null, 3);
        OrderTable orderTable2 = createOrderTable(2L, true, null, 3);
        TableGroup tableGroup = createTableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(orderTable, orderTable2));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(savedTableGroup.getId()).isEqualTo(1L),
            () -> assertThat(savedTableGroup.getOrderTables()).containsAll(Arrays.asList(orderTable, orderTable2))
        );
    }

    @DisplayName("주문 테이블 중 주문이 안 들어가거나 계산 완료되지 않은 테이블이 있을 경우 단체지정 해제할 때 예외를 발생한다.")
    @Test
    void test() {
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);
        assertThatThrownBy(
            () -> tableGroupService.ungroup(1L)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTable = createOrderTable(1L, true, 1L, 3);
        OrderTable orderTable2 = createOrderTable(2L, true, 1L, 3);

        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        tableGroupService.ungroup(1L);

        verify(orderTableDao, VerificationModeFactory.atMost(orderTables.size()))
            .save(any());
    }
}
