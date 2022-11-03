package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블은 그룹으로 묶일 수 있다.")
    void create() {
        final TableGroup tableGroup = 테이블을_그룹으로_묶는다(빈_테이블1, 빈_테이블2);
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        assertAll(
                () -> assertThat(orderTables).extracting("id")
                        .contains(빈_테이블1.getId(), 빈_테이블2.getId()),
                () -> assertThat(orderTables).extracting("tableGroupId")
                        .contains(tableGroup.getId(), tableGroup.getId())
        );
    }

    @Test
    @DisplayName("비어있지 않은 테이블은 그룹으로 묶을 수 없다.")
    void createNotEmpty() {
        assertThatThrownBy(() -> 테이블을_그룹으로_묶는다(손님있는_테이블, 손님있는_식사중_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 두개 이하인 테이블 그룹은 있을 수 없다.")
    void createUnder2() {
        assertThatThrownBy(() -> 테이블을_그룹으로_묶는다(빈_테이블1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리중인 테이블이 있으면 그룹을 해제할 수 없다.")
    void ungroupWithCookingTable() {
        final TableGroup tableGroup = 테이블을_그룹으로_묶는다(빈_테이블1, 빈_테이블2);
        주문_요청한다(빈_테이블1, 파스타한상.getId());

        assertThatThrownBy(() -> 테이블_그룹을_해제한다(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("밥먹는 테이블이 있으면 그룹을 해제할 수 없다.")
    void ungroupWithMealTable() {
        final TableGroup tableGroup = 테이블을_그룹으로_묶는다(빈_테이블1, 빈_테이블2);
        final Order order = 주문_요청한다(빈_테이블1, 파스타한상.getId());
        주문을_식사_상태로_만든다(order);

        assertThatThrownBy(() -> 테이블_그룹을_해제한다(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("그룹으로 묶일 때 테이블이 채워진다.")
    void groupNotEmpty() {
        final TableGroup tableGroup = 테이블을_그룹으로_묶는다(빈_테이블1, 빈_테이블2);

        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        assertThat(orderTables).extracting("empty")
                .contains(false, false);
    }
}