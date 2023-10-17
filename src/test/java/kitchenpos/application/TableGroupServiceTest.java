package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TableGroupServiceTest extends MockServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Test
    void 테이블그룹을_추가한다() {
        // given
        /*
        tableGroupService.create 의 argument 설정
         */
        OrderTable argumentFirstOrderTable = new OrderTable();
        argumentFirstOrderTable.setId(1L);
        OrderTable argumentSecondOrderTable = new OrderTable();
        argumentSecondOrderTable.setId(2L);

        TableGroup argumentTableGroup = new TableGroup();
        argumentTableGroup.setOrderTables(List.of(argumentFirstOrderTable, argumentSecondOrderTable));

        /*
        orderTableDao.findAllByIdIn 의 반환 값 설정
         */
        OrderTable mockReturnFirstOrderTable = new OrderTable();
        mockReturnFirstOrderTable.setId(1L);
        mockReturnFirstOrderTable.setEmpty(true);
        OrderTable mockReturnSecondOrderTable = new OrderTable();
        mockReturnSecondOrderTable.setId(2L);
        mockReturnSecondOrderTable.setEmpty(true);
        BDDMockito.given(orderTableDao.findAllByIdIn(BDDMockito.anyList()))
                .willReturn(List.of(mockReturnFirstOrderTable, mockReturnSecondOrderTable));

        BDDMockito.given(tableGroupDao.save(argumentTableGroup))
                .willReturn(argumentTableGroup);

        // when
        TableGroup actual = tableGroupService.create(argumentTableGroup);
        List<Boolean> actualOrderTableEmpty = actual.getOrderTables().stream()
                .map(OrderTable::isEmpty)
                .collect(Collectors.toList());

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actualOrderTableEmpty.size()).isEqualTo(2);
        softAssertions.assertThat(actualOrderTableEmpty).containsExactlyInAnyOrderElementsOf(List.of(false, false));
        softAssertions.assertAll();
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_포함되어_있지_않으면_예외를_던진다() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_2개_미만이면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(List.of(orderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_존재하지_않는_주문테이블이면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setId(1L);
        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setId(2L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(List.of(firstOrderTable, secondOrderTable));

        OrderTable mockReturnFirstOrderTable = new OrderTable();
        mockReturnFirstOrderTable.setId(1L);
        mockReturnFirstOrderTable.setEmpty(true);
        BDDMockito.given(orderTableDao.findAllByIdIn(
                        List.of(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(
                        List.of(mockReturnFirstOrderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_주문이_가능한_상태이면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setId(1L);
        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setId(2L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(List.of(firstOrderTable, secondOrderTable));

        OrderTable mockReturnFirstOrderTable = new OrderTable();
        mockReturnFirstOrderTable.setId(1L);
        mockReturnFirstOrderTable.setEmpty(false);
        OrderTable mockReturnSecondOrderTable = new OrderTable();
        mockReturnSecondOrderTable.setId(2L);
        mockReturnSecondOrderTable.setEmpty(true);

        BDDMockito.given(orderTableDao.findAllByIdIn(
                        List.of(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(
                        List.of(mockReturnFirstOrderTable, mockReturnSecondOrderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_이미_다른_테이블_그룹에_속해있으면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setId(1L);
        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setId(2L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(List.of(firstOrderTable, secondOrderTable));

        OrderTable mockReturnFirstOrderTable = new OrderTable();
        mockReturnFirstOrderTable.setId(1L);
        mockReturnFirstOrderTable.setEmpty(true);
        OrderTable mockReturnSecondOrderTable = new OrderTable();
        mockReturnSecondOrderTable.setTableGroupId(100L);
        mockReturnSecondOrderTable.setId(2L);
        mockReturnSecondOrderTable.setEmpty(true);

        BDDMockito.given(orderTableDao.findAllByIdIn(
                        List.of(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(
                        List.of(mockReturnFirstOrderTable, mockReturnSecondOrderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_삭제하면_주문테이블이_주문이_가능한_상태로_바뀌고_테이블_그룹_아이디가_null_로_바뀐다() {
        // given
        Long tableGroupId = 1L;

        OrderTable mockReturnFirstOrderTable = new OrderTable();
        mockReturnFirstOrderTable.setId(1L);
        mockReturnFirstOrderTable.setEmpty(true);
        OrderTable mockReturnSecondOrderTable = new OrderTable();
        mockReturnSecondOrderTable.setTableGroupId(100L);
        mockReturnSecondOrderTable.setId(2L);
        mockReturnSecondOrderTable.setEmpty(true);
        BDDMockito.given(orderTableDao.findAllByTableGroupId(tableGroupId))
                .willReturn(List.of(mockReturnFirstOrderTable, mockReturnSecondOrderTable));

        BDDMockito.given(
                        orderDao.existsByOrderTableIdInAndOrderStatusIn(
                                List.of(mockReturnFirstOrderTable.getId(), mockReturnSecondOrderTable.getId()),
                                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
                        ))
                .willReturn(false);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(mockReturnFirstOrderTable.getTableGroupId()).isNull();
        softAssertions.assertThat(mockReturnSecondOrderTable.getTableGroupId()).isNull();
        softAssertions.assertThat(mockReturnFirstOrderTable.isEmpty()).isFalse();
        softAssertions.assertThat(mockReturnSecondOrderTable.isEmpty()).isFalse();
        softAssertions.assertAll();
    }

    @Test
    void 테이블그룹을_삭제할_때_주문테이블_내에_존재하는_주문들_중_COOKING_또는_MEAL_상태가_존재하면_예외를_던진다() {
        // given
        Long tableGroupId = 1L;

        OrderTable mockReturnFirstOrderTable = new OrderTable();
        mockReturnFirstOrderTable.setId(1L);
        mockReturnFirstOrderTable.setEmpty(true);
        OrderTable mockReturnSecondOrderTable = new OrderTable();
        mockReturnSecondOrderTable.setTableGroupId(100L);
        mockReturnSecondOrderTable.setId(2L);
        mockReturnSecondOrderTable.setEmpty(true);
        BDDMockito.given(orderTableDao.findAllByTableGroupId(tableGroupId))
                .willReturn(List.of(mockReturnFirstOrderTable, mockReturnSecondOrderTable));

        BDDMockito.given(
                        orderDao.existsByOrderTableIdInAndOrderStatusIn(
                                List.of(mockReturnFirstOrderTable.getId(), mockReturnSecondOrderTable.getId()),
                                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
                        ))
                .willReturn(true);

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
