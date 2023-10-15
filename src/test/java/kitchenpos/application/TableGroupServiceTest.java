package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixtures.Fixtures;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    Fixtures fixtures;

    @Autowired
    TableGroupService tableGroupService;

    @Nested
    class 단체_지정_등록 {

        @Test
        void 단체_지정을_등록한다() {
            // given
            OrderTable orderTableA = fixtures.빈_테이블_저장();
            OrderTable orderTableB = fixtures.빈_테이블_저장();

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTableA, orderTableB));

            // when
            TableGroup result = tableGroupService.create(tableGroup);

            // then
            List<Long> orderTableIds = result.getOrderTables()
                    .stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());

            assertThat(orderTableIds).contains(orderTableA.getId(), orderTableB.getId());
        }

        @Test
        void 주문_테이블이_1개_인경우_예외가_발생한다() {
            // given
            OrderTable orderTableA = fixtures.빈_테이블_저장();

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTableA));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        @Test
        void 주문_테이블이_null인_경우_예외가_발생한다() {
            // given
            TableGroup tableGroup = new TableGroup();

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        @Test
        void 주문_테이블이_존재하지_않는_경우_예외가_발생한다() {
            // given
            OrderTable orderTableA = fixtures.빈_테이블_저장();
            OrderTable orderTableB = new OrderTable();

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTableA, orderTableB));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있지_않는_경우_예외가_발생한다() {
            // given
            OrderTable orderTableA = fixtures.빈_테이블_저장();
            OrderTable orderTableB = fixtures.주문_테이블_저장();

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTableA, orderTableB));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_단체_지정이_등록이_되어있는_경우_예외가_발생한다() {
            // given
            TableGroup tableGroup = fixtures.단체_지정_저장();
            OrderTable orderTableA = fixtures.주문_테이블_저장(tableGroup.getId(), true);
            OrderTable orderTableB = fixtures.주문_테이블_저장(tableGroup.getId(), true);

            TableGroup newTableGroup = new TableGroup();
            OrderTable orderTableC = fixtures.빈_테이블_저장();
            newTableGroup.setOrderTables(List.of(orderTableA, orderTableC));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("1234");
        }
    }

    @Nested
    class 단체_지정_헤제 {
        @Test
        void 단체_지정을_헤제한다() {
            // given
            TableGroup tableGroup = fixtures.단체_지정_저장();
            fixtures.주문_테이블_저장(tableGroup.getId(), true);
            fixtures.주문_테이블_저장(tableGroup.getId(), true);

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            List<OrderTable> orderTables = orderTableDao.findAll();

            assertThat(orderTables.get(0).getTableGroupId()).isNull();
            assertThat(orderTables.get(1).getTableGroupId()).isNull();
        }

        @Test
        void 주문_테이블_상태가_계산완료가_아닌_경우_예외가_발생한다() {
            // given
            TableGroup tableGroup = fixtures.단체_지정_저장();
            OrderTable orderTableA = fixtures.주문_테이블_저장(tableGroup.getId(), true);
            OrderTable orderTableB = fixtures.주문_테이블_저장(tableGroup.getId(), true);

            fixtures.주문_저장(orderTableA.getId(), OrderStatus.COOKING);
            fixtures.주문_저장(orderTableB.getId(), OrderStatus.COMPLETION);

            // when
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
