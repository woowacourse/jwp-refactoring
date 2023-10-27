package kitchenpos.application;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.ordertable.exception.NotExistOrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.exception.InvalidTableGroupException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
@Sql("/truncate.sql")
class TableGroupServiceTest {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderTableRepository orderTableRepository;
    
    @Autowired
    TableGroupService tableGroupService;
    
    @Test
    void 단체_테이블을_생성할_때_개별_테이블의_개수가_1개_이하이면_예외가_발생한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        
        //when & then
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(savedOrderTable1.getId()));
        
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(InvalidTableGroupException.class)
                .hasMessageContaining("개별 테이블 수가 2개 미만이면 단체 테이블을 만들 수 없습니다");
    }
    
    @Test
    void 단체_테이블을_생성할_때_존재하지_않는_테이블이_있으면_예외가_발생한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        Long notExistOrderTableId = 2L;
        
        //when & then
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(savedOrderTable1.getId(), notExistOrderTableId));
        
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(NotExistOrderTable.class)
                .hasMessageContaining("존재하지 않는 테이블입니다");
    }
    
    @Test
    void 단체_테이블을_생성할_때_빈_테이블이_아닌_테이블이_있으면_예외가_발생한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        OrderTable EmptyTable = ORDER_TABLE(false, 1);
        OrderTable savedEmptyTable2 = orderTableRepository.save(EmptyTable);
        
        //when & then
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(savedOrderTable1.getId(), savedEmptyTable2.getId()));
        
        
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(InvalidTableGroupException.class)
                .hasMessageContaining("비어있지 않은 테이블은 단체 테이블으로 만들 수 없습니다");
    }
    
    @Test
    void 단체_테이블을_생성할_때_이미_다른_테이블_그룹에_속하는_테이블이_있으면_예외가_발생한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        
        OrderTable orderTableInAnotherTableGroup1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTableInAnotherTableGroup1 = orderTableRepository.save(orderTableInAnotherTableGroup1);
        OrderTable orderTableInAnotherTableGroup2 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTableInAnotherTableGroup2 = orderTableRepository.save(orderTableInAnotherTableGroup2);
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(savedOrderTableInAnotherTableGroup1.getId(), savedOrderTableInAnotherTableGroup2.getId()));
        TableGroup anotherTableGroup = tableGroupService.create(tableGroupCreateRequest);
        orderTableInAnotherTableGroup1.setTableGroup(anotherTableGroup.getId());
        orderTableInAnotherTableGroup2.setTableGroup(anotherTableGroup.getId());
        
        //when & then
        TableGroupCreateRequest tableGroupWithTableInAnotherTableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(savedOrderTableInAnotherTableGroup1.getId(), savedOrderTable1.getId()));
        
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithTableInAnotherTableGroupCreateRequest))
                .isInstanceOf(InvalidTableGroupException.class)
                .hasMessageContaining("다른 테이블 그룹에 속한 테이블은 단체 테이블로 만들 수 없습니다");
    }
    
    @Test
    void 단체_테이블을_생성할_때_모든_조건을_만족하면_정상적으로_단체_테이블을_생성한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        
        OrderTable orderTable2 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
        
        //when
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(savedOrderTable1.getId(), savedOrderTable2.getId()));
        TableGroup actual = tableGroupService.create(tableGroupCreateRequest);
        List<OrderTable> actualOrderTablesInOrderGroup = orderTableRepository.findAllByTableGroupId(actual.getId());
        
        //then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNotNull();
            softly.assertThat(actual.getOrderTables())
                  .usingRecursiveComparison()
                  .isEqualTo(actualOrderTablesInOrderGroup);
        });
    }
    
}
