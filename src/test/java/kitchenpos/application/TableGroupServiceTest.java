package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    private List<OrderTable> standardOrderTables;
    private TableGroup standardTableGroup;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setId(1L);
        firstOrderTable.setEmpty(true);
        firstOrderTable.setNumberOfGuests(5);

        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setId(2L);
        secondOrderTable.setEmpty(true);
        secondOrderTable.setNumberOfGuests(5);

        standardOrderTables = new LinkedList<>();
        standardOrderTables.add(firstOrderTable);
        standardOrderTables.add(secondOrderTable);

        standardTableGroup = new TableGroup();
        standardTableGroup.setId(1L);
        standardTableGroup.setCreatedDate(LocalDateTime.MAX);
        standardTableGroup.setOrderTables(standardOrderTables);
    }

    @DisplayName("단체를 생성할 때, 주문 테이블이 비어선 안 된다.")
    @Test
    void createTableGroupWithNullOrderTables() {
        //given
        standardTableGroup.setOrderTables(null);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성할 때, 주문 테이블이 2개보다 작아선 안 된다.")
    @Test
    void createTableGroupWithBelowTwoOrderTables() {
        //given
        List<OrderTable> zeroOrderTables = new LinkedList<>();
        standardTableGroup.setOrderTables(zeroOrderTables);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성할 때, 저장한 주문 테이블과 원래 테이블의 갯수가 달라선 안된다.")
    @Test
    void createTableGroupWithChangedTableNumbers() {
        //given
        List<OrderTable> zeroOrderTables = new LinkedList<>();
        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(zeroOrderTables);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성할 때, 저장한 주문 테이블이 비어있어야만 한다.")
    @Test
    void createTableGroupWithFullTable() {
        //given
        standardTableGroup.getOrderTables()
            .forEach(table -> table.setEmpty(false));
        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(standardOrderTables);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성할 때, 저장된 테이블들이 테이블 그룹에 할당되어 있으면 안된다.")
    @Test
    void createTableGroupWithGrouppedTable() {
        //given
        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(standardOrderTables);
        standardTableGroup.getOrderTables()
            .forEach(table -> table.setTableGroupId(1L));

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성한다.")
    @Test
    void createTableGroup() {
        //given
        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(standardOrderTables);
        given(tableGroupDao.save(standardTableGroup)).willReturn(standardTableGroup);

        //when
        TableGroup tableGroup = tableGroupService.create(standardTableGroup);

        //then
        assertAll(
            () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(2),
            () -> assertThat(tableGroup.getId()).isEqualTo(1L),
            () -> assertThat(tableGroup.getCreatedDate()).isNotEqualTo(LocalDateTime.MAX)
        );
    }

    @DisplayName("단체를 삭제시, 요리하거나 식사중인 테이블이 없어야만 한다.")
    @Test
    void deleteGroupWithCookOrMeal() {
        //given
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(standardOrderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(1L, 2L),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
