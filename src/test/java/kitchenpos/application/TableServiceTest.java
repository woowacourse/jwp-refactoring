package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends IntegrationTest {

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable2 = new OrderTable();
    }

    @Test
    void 주문_테이블을_저장한다() {
        // when
        OrderTable result = tableService.create(orderTable1);

        // then
        assertThat(result.getId()).isPositive();
    }

    @Test
    void 주문_테이블들을_조회한다() {
        // given
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        // when
        List<OrderTable> result = tableService.list();

        // then
        Assertions.assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(savedOrderTable1.getId()),
                () -> assertThat(result.get(1).getId()).isEqualTo(savedOrderTable2.getId())
        );
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 존재하지_않는_주문_테이블을_변경하면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 지정된_단체가_있는_테이블을_변경하면_예외가_발생한다() {
            // given
            orderTable1.setEmpty(true);
            orderTable2.setEmpty(true);
            OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
            OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            orderTable1.setTableGroupId(savedTableGroup.getId());
            Long id = orderTableDao.save(orderTable1).getId();

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(id, orderTable2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 조리중이거나_식사중인_테이블을_변경하면_예외가_발생한다() {
            // given
            orderTable1.setEmpty(true);
            OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
            주문(savedOrderTable1, OrderStatus.COOKING, 맛있는_메뉴());

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), orderTable2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_상태를_변경한다() {
            // given
            orderTable1.setEmpty(true);
            OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

            // when
            OrderTable result = tableService.changeEmpty(savedOrderTable1.getId(), orderTable2);

            // then
            assertThat(result.isEmpty()).isFalse();
        }
    }
}
