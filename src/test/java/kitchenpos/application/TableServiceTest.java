package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.OrderFixture.ORDER_ID_STATUS;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static kitchenpos.fixture.TableGroupFixture.TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class TableServiceTest {
    
    @Autowired
    TableService tableService;
    
    @Autowired
    OrderTableDao orderTableDao;
    
    @Autowired
    TableGroupDao tableGroupDao;
    
    @Autowired
    OrderDao orderDao;
    
    @Test
    void 주문_테이블을_정상적으로_생성한다() {
        //given
        final OrderTable orderTable = ORDER_TABLE(false, 1);
        
        //when
        final OrderTable savedOrderTable = tableService.create(orderTable);
        
        //then
        assertSoftly(softly -> {
                    softly.assertThat(orderTable.getId()).isNull();
                    softly.assertThat(orderTable.getTableGroupId()).isNull();
                }
        );
    }
    
    @Test
    void 주문_테이블_목록을_조회한다() {
        //given
        final OrderTable orderTable1 = ORDER_TABLE(false, 1);
        final OrderTable orderTable2 = ORDER_TABLE(false, 1);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        
        //when
        List<OrderTable> expected = List.of(savedOrderTable1, savedOrderTable2);
        List<OrderTable> actual = tableService.list();
        
        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
    
    @Test
    void 주문_테이블의_상태를_변경하려고_할_때_테이블이_단체_테이블에_속하면_주문_테이블의_상태를_변경한다() {
        //given
        final TableGroup tableGroup = TABLE_GROUP(List.of());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        final OrderTable orderTable = ORDER_TABLE(false, 1);
        orderTable.setTableGroupId(savedTableGroup.getId());
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        
        //when & then
        final OrderTable orderTableToChange = ORDER_TABLE(true, 1);
        
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTableToChange))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 테이블이면 테이블의 상태를 변경할 수 없습니다");
        
    }
    
    @Test
    void 주문_테이블의_상태를_변경하려고_할_때_테이블에_속한_주문의_상태는_주문_테이블의_상태를_변경한다() {
        //given
        final OrderTable orderTable = ORDER_TABLE(false, 1);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        final Order orderWithCookingStatus = ORDER_ID_STATUS(savedOrderTable.getId(), COOKING);
        orderDao.save(orderWithCookingStatus);
        
        //when & then
        final OrderTable orderTableToChange = ORDER_TABLE(true, 1);
        
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTableToChange))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블에 속하는 주문의 상태가 COOKING 또는 MEAL이라면 테이블의 상태를 변경할 수 없습니다");
    }
    
    @Test
    void 주문_테이블의_상태를_변경하려고_할_때_모든_조건을_만족하면_주문_테이블의_상태를_변경한다() {   //given
        //given
        final OrderTable orderTable = ORDER_TABLE(false, 1);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        
        //when
        final OrderTable orderTableToChange = ORDER_TABLE(true, 1);
        tableService.changeEmpty(savedOrderTable.getId(), orderTableToChange);
        
        //then
        OrderTable changedOrderTable = orderTableDao.findById(savedOrderTable.getId()).get();
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }
    
    @Test
    void 주문_테이블의_손님_수를_변경하려고_할_때_테이블의_손님_수가_0_미만이면_예외가_발생한다() {   //given
        //given
        final OrderTable orderTable = ORDER_TABLE(false, 2);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        
        //when & then
        final OrderTable orderTableToChange = ORDER_TABLE(false, -1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableToChange))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("바꾸려는 손님의 수가 0명 미만이면 테이블 손님 수를 변경할 수 없습니다");
    }
    
    @Test
    void 주문_테이블의_손님_수를_변경하려고_할_때_테이블이_빈_테이블이면_예외가_발생한다() {
        //given
        final OrderTable orderTable = ORDER_TABLE(true, 2);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        
        //when & then
        final OrderTable orderTableToChange = ORDER_TABLE(true, 3);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableToChange))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블의 손님 수를 변경할 수 없습니다");
    }
    
    @Test
    void 주문_테이블의_손님_수를_변경하려고_모든_조건을_만족하면_테이블의_손님_수를_변경한다() {
        //given
        final OrderTable orderTable = ORDER_TABLE(false, 2);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        
        //when
        final OrderTable orderTableToChange = ORDER_TABLE(false, 3);
        tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableToChange);
        
        //then
        OrderTable changedOrderTable = orderTableDao.findById(savedOrderTable.getId()).get();
        assertThat(changedOrderTable.getNumberOfGuests())
                .isEqualTo(3);
    }
}
