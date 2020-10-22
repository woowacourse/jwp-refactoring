package kitchenpos.application;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.factory.OrderTableFactory;
import kitchenpos.factory.TableGroupFactory;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTableFactory orderTableFactory;
    private TableGroupFactory tableGroupFactory;

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTableFactory = new OrderTableFactory();
        tableGroupFactory = new TableGroupFactory();
        orderTable1 = orderTableFactory.create(1L, null, true);
        orderTable2 = orderTableFactory.create(2L, null, true);
        tableGroup = tableGroupFactory.create(asList(orderTable1, orderTable2));
    }

    @DisplayName("단체 지정 생성")
    @TestFactory
    Stream<DynamicTest> create() {
        return Stream.of(
                dynamicTest("단체 지정을 생성한다.", this::createSuccess),
                dynamicTest("OrderTable이 존재해야 한다.", this::noOrderTable),
                dynamicTest("요청엔 2개 이상의 테이블이 존재해야 한다.", this::invalidOrderTable),
                dynamicTest("요청한 테이블들이 하나씩 존재해야 한다.", this::orderTableMismatch),
                dynamicTest("테이블에 주문이 없어야 한다.", this::orderTableHasOrder),
                dynamicTest("단체 지정이 되어있지 않아야 한다.", this::orderTableHasTableGroup)
        );
    }

    @DisplayName("단체 지정 해제")
    @TestFactory
    Stream<DynamicTest> ungroup() {
        return Stream.of(
                dynamicTest("단체 지정을 해제한다.", this::ungroupSuccess),
                dynamicTest("테이블에 모든 주문은 완료 상태이어야 한다.", this::invalidOrderStatus)
        );
    }

    private void createSuccess() {
        TableGroup expected = tableGroupFactory.create(1L, asList(orderTable1, orderTable2));
        OrderTable saved1 = orderTableFactory.create(1L, expected.getId(), false);
        OrderTable saved2 = orderTableFactory.create(2L, expected.getId(), false);

        given(orderTableDao.findAllByIdIn(asList(orderTable1.getId(), orderTable2.getId())))
                .willReturn(asList(orderTable1, orderTable2));
        given(tableGroupDao.save(tableGroup)).willReturn(expected);
        given(orderTableDao.save(orderTable1)).willReturn(saved1);
        given(orderTableDao.save(orderTable2)).willReturn(saved2);

        TableGroup actual = tableGroupService.create(tableGroup);
        List<OrderTable> actualOrderTables = actual.getOrderTables();

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getCreatedDate()).isEqualTo(expected.getCreatedDate()),
                () -> assertThat(actualOrderTables.size()).isEqualTo(
                        expected.getOrderTables().size()),
                () -> assertThat(actualOrderTables.get(0).getId()).isEqualTo(saved1.getId()),
                () -> assertThat(actualOrderTables.get(1).getId()).isEqualTo(saved2.getId())
        );
    }

    private void noOrderTable() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(new TableGroup()));
    }

    private void invalidOrderTable() {
        TableGroup tableGroup = tableGroupFactory.create(singletonList(new OrderTable()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    private void orderTableMismatch() {
        given(orderTableDao.findAllByIdIn(asList(orderTable1.getId(), orderTable2.getId())))
                .willReturn(singletonList(new OrderTable()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    private void orderTableHasOrder() {
        OrderTable saved1 = orderTableFactory.create(1L, false);
        OrderTable saved2 = orderTableFactory.create(2L, true);

        given(orderTableDao.findAllByIdIn(asList(orderTable1.getId(), orderTable2.getId())))
                .willReturn(asList(saved1, saved2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    private void orderTableHasTableGroup() {
        OrderTable saved1 = orderTableFactory.create(1L, 1L, true);
        OrderTable saved2 = orderTableFactory.create(2L, true);

        given(orderTableDao.findAllByIdIn(asList(orderTable1.getId(), orderTable2.getId())))
                .willReturn(asList(saved1, saved2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    private void ungroupSuccess() {
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                asList(orderTable1.getId(), orderTable2.getId()),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        tableGroupService.ungroup(tableGroup.getId());

        verify(orderTableDao).save(orderTable1);
        verify(orderTableDao).save(orderTable2);
    }

    private void invalidOrderStatus() {
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                asList(orderTable1.getId(), orderTable2.getId()),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
    }
}
