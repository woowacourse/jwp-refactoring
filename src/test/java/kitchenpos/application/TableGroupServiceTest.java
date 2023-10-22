package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.OrderFixture.ORDER;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static kitchenpos.fixture.TableGroupFixture.TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class TableGroupServiceTest {
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private OrderTableDao orderTableDao;
    
    @Autowired
    TableGroupService tableGroupService;
    
    @Test
    void 단체_테이블을_생성할_때_개별_테이블의_개수가_1개_이하이면_예외가_발생한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        
        //when & then
        TableGroup tableGroupWithOneTable = TABLE_GROUP(List.of(savedOrderTable1));
        
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithOneTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("개별 테이블 수가 2개 미만이면 단체 테이블을 만들 수 없습니다");
    }
    
    @Test
    void 단체_테이블을_생성할_때_존재하지_않는_테이블이_있으면_예외가_발생한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable orderTable2 = ORDER_TABLE(true, 1);
        
        //when & then
        TableGroup tableGroupWithNotExistTable = TABLE_GROUP(List.of(savedOrderTable1, orderTable2));
        
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithNotExistTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 테이블이 있습니다");
    }
    
    @Test
    void 단체_테이블을_생성할_때_빈_테이블이_아닌_테이블이_있으면_예외가_발생한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable EmptyTable = ORDER_TABLE(false, 1);
        OrderTable savedEmptyTable = orderTableDao.save(EmptyTable);
        
        //when & then
        TableGroup tableGroupWithEmptyTable = TABLE_GROUP(List.of(savedOrderTable1, savedEmptyTable));
        
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithEmptyTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있지 않은 테이블은 단체 테이블으로 만들 수 없습니다");
    }
    
    @Test
    void 단체_테이블을_생성할_때_이미_다른_테이블_그룹에_속하는_테이블이_있으면_예외가_발생한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        
        OrderTable orderTableInAnotherTableGroup1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTableInAnotherTableGroup1 = orderTableDao.save(orderTableInAnotherTableGroup1);
        OrderTable orderTableInAnotherTableGroup2 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTableInAnotherTableGroup2 = orderTableDao.save(orderTableInAnotherTableGroup2);
        TableGroup anotherTableGroup = tableGroupService.create(TABLE_GROUP(List.of(savedOrderTableInAnotherTableGroup1, savedOrderTableInAnotherTableGroup2)));
        
        //when & then
        OrderTable orderTableInAnotherTableGroup = anotherTableGroup.getOrderTables().get(1);
        orderTableInAnotherTableGroup.changeEmpty(true);
        TableGroup tableGroupWithTableInAnotherTableGroup = TABLE_GROUP(List.of(savedOrderTable1, orderTableInAnotherTableGroup));
        
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithTableInAnotherTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("다른 테이블 그룹에 속한 테이블은 단체 테이블로 만들 수 없습니다");
    }
    
    @Test
    void 단체_테이블을_생성할_때_모든_조건을_만족하면_정상적으로_단체_테이블을_생성한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        
        OrderTable orderTable2 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        
        //when
        TableGroup tableGroup = TABLE_GROUP(List.of(savedOrderTable1, savedOrderTable2));
        TableGroup actual = tableGroupService.create(tableGroup);
        List<OrderTable> actualOrderTablesInOrderGroup = orderTableDao.findAllByTableGroupId(actual.getId());
        
        //then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNotNull();
            softly.assertThat(actual.getOrderTables())
                  .usingRecursiveComparison()
                  .isEqualTo(actualOrderTablesInOrderGroup);
        });
    }
    
    @Test
    void 단체_테이블을_해제할_때_존재하지않는_테이블이_있으면_예외를_발생한다() {
        //given
        OrderTable orderTable = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable);
        
        OrderTable orderTable2 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        
        List<OrderTable> orderTables = List.of(savedOrderTable1, savedOrderTable2);
        TableGroup tableGroup = TABLE_GROUP(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        
        //when & then
        Order order1 = ORDER(savedOrderTable1, List.of(), OrderStatus.COOKING);
        orderDao.save(order1);
        
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블의 상태가 COOKING 혹은 MEAL이면 단체 테이블을 해제할 수 없습니다");
    }
    
    @Test
    void 단체_테이블을_해제할_때_모든_조건을_만족하면_개별_테이블들을_해제한다() {
        //given
        OrderTable orderTable = ORDER_TABLE(true, 1);
        OrderTable orderTable1 = orderTableDao.save(orderTable);
        
        OrderTable orderTable2 = ORDER_TABLE(true, 1);
        OrderTable save = orderTableDao.save(orderTable2);
        
        List<OrderTable> orderTables = List.of(orderTable1, save);
        TableGroup tableGroup = TABLE_GROUP(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        
        Order order1 = ORDER(orderTable1, List.of(), OrderStatus.COMPLETION);
        orderDao.save(order1);
        
        //when : 저장된 테이블의 id로, 테이블의 현재 단체 테이블 id를 추적한다.
        List<OrderTable> orderTablesInTableGroup = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
        final List<Long> orderTableIds = orderTablesInTableGroup.stream()
                                                                .map(OrderTable::getId)
                                                                .collect(Collectors.toList());
        tableGroupService.ungroup(savedTableGroup.getId());
        
        //then : 해제된 모든 개별테이블의 단체테이블id가 null이고, 빈 테이블이 아니다.
        assertSoftly(softly -> {
            for (OrderTable tableNotInTableGroup : orderTableDao.findAllByIdIn(orderTableIds)) {
                softly.assertThat(tableNotInTableGroup.getTableGroup()).isNull();
                softly.assertThat(tableNotInTableGroup.isEmpty()).isFalse();
            }
        });
    }
}
