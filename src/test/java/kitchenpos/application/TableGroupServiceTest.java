package kitchenpos.application;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);
        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        tableGroup = new TableGroup();
        tableGroup.setOrderTables(asList(orderTable1, orderTable2));
    }

    @DisplayName("단체 지정 생성")
    @TestFactory
    Stream<DynamicTest> create() {
        return Stream.of(
                dynamicTest("단체 지정을 생성한다.", () -> {
                    TableGroup expected = new TableGroup();
                    expected.setId(1L);
                    expected.setOrderTables(asList(orderTable1, orderTable2));
                    OrderTable saved1 = new OrderTable();
                    saved1.setId(1L);
                    saved1.setTableGroupId(expected.getId());
                    saved1.setEmpty(false);
                    OrderTable saved2 = new OrderTable();
                    saved2.setId(2L);
                    saved2.setTableGroupId(expected.getId());
                    saved2.setEmpty(false);

                    given(orderTableDao.findAllByIdIn(
                            asList(orderTable1.getId(), orderTable2.getId())))
                            .willReturn(asList(orderTable1, orderTable2));
                    given(tableGroupDao.save(tableGroup)).willReturn(expected);
                    given(orderTableDao.save(orderTable1)).willReturn(saved1);
                    given(orderTableDao.save(orderTable2)).willReturn(saved2);

                    TableGroup actual = tableGroupService.create(tableGroup);

                    assertThat(actual.getId()).isEqualTo(expected.getId());
                    assertThat(actual.getCreatedDate()).isEqualTo(expected.getCreatedDate());
                    List<OrderTable> actualOrderTables = actual.getOrderTables();
                    assertThat(actualOrderTables.size()).isEqualTo(
                            expected.getOrderTables().size());
                    assertThat(actualOrderTables.get(0).getId()).isEqualTo(saved1.getId());
                    assertThat(actualOrderTables.get(1).getId()).isEqualTo(saved2.getId());
                }),
                dynamicTest("요청에 OrderTable이 없을때, IllegalArgumentException 발생.", () -> {
                    assertThatIllegalArgumentException()
                            .isThrownBy(() -> tableGroupService.create(new TableGroup()));
                }),
                dynamicTest("요청에 OrderTable이 하나만 존재할때, IllegalArgumentException 발생.", () -> {
                    TableGroup tableGroup = new TableGroup();
                    tableGroup.setOrderTables(singletonList(new OrderTable()));
                    assertThatIllegalArgumentException()
                            .isThrownBy(() -> tableGroupService.create(tableGroup));
                }),
                dynamicTest("요청의 OrderTable과 저장된 OrderTable의 크기가 다를때, IllegalArgumentException 발생.",
                        () -> {
                            given(orderTableDao.findAllByIdIn(
                                    asList(orderTable1.getId(), orderTable2.getId())))
                                    .willReturn(singletonList(new OrderTable()));

                            assertThatIllegalArgumentException()
                                    .isThrownBy(() -> tableGroupService.create(tableGroup));
                        }),
                dynamicTest("저장된 OrderTable에 주문이 존재할때, IllegalArgumentException 발생.", () -> {
                    TableGroup expected = new TableGroup();
                    expected.setId(1L);
                    expected.setOrderTables(asList(orderTable1, orderTable2));
                    OrderTable saved1 = new OrderTable();
                    saved1.setId(1L);
                    saved1.setEmpty(false);
                    OrderTable saved2 = new OrderTable();
                    saved2.setId(2L);
                    saved2.setEmpty(true);

                    given(orderTableDao.findAllByIdIn(
                            asList(orderTable1.getId(), orderTable2.getId())))
                            .willReturn(asList(saved1, saved2));

                    assertThatIllegalArgumentException()
                            .isThrownBy(() -> tableGroupService.create(tableGroup));
                }),
                dynamicTest("저장된 OrderTable에 TableGroup이 존재하면, IllegalArgumentException 발생.",
                        () -> {
                            TableGroup expected = new TableGroup();
                            expected.setId(1L);
                            expected.setOrderTables(asList(orderTable1, orderTable2));
                            OrderTable saved1 = new OrderTable();
                            saved1.setId(1L);
                            saved1.setEmpty(true);
                            saved1.setTableGroupId(1L);
                            OrderTable saved2 = new OrderTable();
                            saved2.setId(2L);
                            saved2.setEmpty(true);

                            given(orderTableDao.findAllByIdIn(
                                    asList(orderTable1.getId(), orderTable2.getId())))
                                    .willReturn(asList(saved1, saved2));

                            assertThatIllegalArgumentException()
                                    .isThrownBy(() -> tableGroupService.create(tableGroup));
                        })
        );
    }

    @DisplayName("단체 지정 해제")
    @TestFactory
    Stream<DynamicTest> ungroup() {
        return Stream.of(
                dynamicTest("단체 지정을 해제한다.", () -> {
                    given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                            .willReturn(asList(orderTable1, orderTable2));
                    given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                            asList(orderTable1.getId(), orderTable2.getId()),
                            asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                            .willReturn(false);

                    tableGroupService.ungroup(tableGroup.getId());

                    verify(orderTableDao).save(orderTable1);
                    verify(orderTableDao).save(orderTable2);
                }),
                dynamicTest(
                        "TableGroup의 OrderTable 중 주문 상태가 조리 또는 식사 일때, IllegalArgumentException 발생.",
                        () -> {
                            given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                                    .willReturn(asList(orderTable1, orderTable2));
                            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                                    asList(orderTable1.getId(), orderTable2.getId()),
                                    asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                                    .willReturn(true);

                            assertThatIllegalArgumentException()
                                    .isThrownBy(
                                            () -> tableGroupService.ungroup(tableGroup.getId()));
                        })
        );
    }
}