package kitchenpos.application;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE2;
import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 할 때 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void create_ifOrderTableSizeLessThanTwo_throwsException() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(ORDER_TABLE1.create());
        final TableGroup tableGroup = new TableGroup(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 할 때 주문 테이블과 저장된 주문 테이블의 갯수가 다르면 예외가 발생한다.")
    @Test
    void create_ifContainsNotExistOrderTable_throwsException() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(ORDER_TABLE1.create(), ORDER_TABLE2.createWithIdNull());
        final TableGroup tableGroup = new TableGroup(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable이 빈 테이블이 아니라면 예외가 발생한다.")
    @Test
    void create_ifOrderTableNotEmpty_throwsException() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(ORDER_TABLE1.create(), ORDER_TABLE_NOT_EMPTY.create());
        final TableGroup tableGroup = new TableGroup(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable이 정상적으로 단체 지정이 된다.")
    @Test
    void create() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(ORDER_TABLE1.create(), ORDER_TABLE2.create());
        final TableGroup tableGroup = new TableGroup(orderTables);

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertAll(
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2)
        );
    }
}
