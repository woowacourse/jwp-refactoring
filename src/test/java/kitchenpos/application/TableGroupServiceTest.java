package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

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

    TableGroup tableGroup;
    List<OrderTable> orderTables;

    @BeforeEach
    void beforeEach() {
        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.now());

        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(3);
        orderTable1.setTableGroupId(null);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setNumberOfGuests(5);
        orderTable2.setTableGroupId(null);

        orderTables = List.of(orderTable1, orderTable2);
    }

    @DisplayName("tableGroup을 생성한다.")
    @Test
    void tableGroup_생성() {
        //given
        final TableGroup expected = tableGroup;
        expected.setOrderTables(orderTables);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(orderTableIds))
                .willReturn(orderTables);
        given(tableGroupDao.save(expected))
                .willReturn(expected);
        for (final OrderTable orderTable : orderTables) {
            orderTable.setEmpty(true);
            given(orderTableDao.save(orderTable))
                    .willReturn(orderTable);
        }

        //when
        final TableGroup actual = tableGroupService.create(expected);

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
        });
    }

    @DisplayName("orderTables가 비어있으면 예외를 던진다.")
    @Test
    void orderTables가_비어있으면_예외() {
        //given
        final TableGroup expected = tableGroup;
        expected.setOrderTables(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTables의 크기가 2개 미만이면 예외를 던진다.")
    @Test
    void orderTables의_크기가_2개미만이면_예외() {
        //given
        final TableGroup expected = tableGroup;
        expected.setOrderTables(List.of(orderTables.get(0)));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTable이 empty가 true가 아니면 예외를 던진다.")
    @Test
    void orderTable이_empty가_false면_예외() {
        //given
        final TableGroup expected = tableGroup;
        expected.setOrderTables(orderTables);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(orderTableIds))
                .willReturn(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.setEmpty(false);
        }

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTable의 tableGroupId가 null이 아니면 예외를 던진다.")
    @Test
    void orderTable의_tableGroupId가_null이_아니면_예외() {
        //given
        final TableGroup expected = tableGroup;
        expected.setOrderTables(orderTables);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(orderTableIds))
                .willReturn(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.setEmpty(true);
            orderTable.setTableGroupId(1L);
        }

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("tableGroup을 unGroup한다.")
    @Test
    void tableGroup을_unGroup() {
        final Long tableGroupId = tableGroup.getId();
        given(orderTableDao.findAllByTableGroupId(tableGroupId))
                .willReturn(orderTables);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        for (final OrderTable orderTable : orderTables) {
            given(orderTableDao.save(orderTable))
                    .willReturn(orderTable);
        }

        //when
        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
    }

    @DisplayName("tableGroup의 orderTable 중 cooking이나 meal상태인 것이 있으면 unGroup할 때 예외를 던진다.")
    @Test
    void unGroup할_때_cooking이나_meal인_orderTable이_있으면_예외() {
        final Long tableGroupId = tableGroup.getId();
        given(orderTableDao.findAllByTableGroupId(tableGroupId))
                .willReturn(orderTables);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
