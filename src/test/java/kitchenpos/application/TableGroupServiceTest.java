package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {


    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableDao orderTableDao;


    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;
//
//    @BeforeEach
//    void init() {
//        firstOrderTable = orderTableDao.save(OrderTableFixture.create(true, 0));
//        secondOrderTable = orderTableDao.save(OrderTableFixture.create(true, 0));
//    }
//
//    @Nested
//    class 단체_테이블을_등록할_때 {
//
//        @Test
//        void success() {
//            // given
//            TableGroup tableGroup = TableGroupFixture.create(List.of(firstOrderTable, secondOrderTable));
//
//            // when
//            TableGroup actual = tableGroupService.create(tableGroup);
//
//            // then
//            List<OrderTable> updatedOrderTables = actual.getOrderTables();
//            List<Long> updatedOrderTableIds = updatedOrderTables.stream().map(OrderTable::getId)
//                    .collect(Collectors.toList());
//
//            assertThat(updatedOrderTables).hasSize(2);
//            assertThat(updatedOrderTableIds).containsExactly(firstOrderTable.getId(), secondOrderTable.getId());
//        }
//
//        @Test
//        void 테이블이_비어있으면_실패() {
//            // given
//            TableGroup tableGroup = TableGroupFixture.create(Collections.emptyList());
//
//            // when
//            // then
//            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        void 테이블이_2개_미만이면_실패() {
//            // given
//            TableGroup tableGroup = TableGroupFixture.create(List.of(firstOrderTable));
//
//            // when
//            // then
//            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        void 등록되지_않은_주문테이블이_존재할_경우_실패() {
//            // given
//            TableGroup tableGroup = TableGroupFixture.create(List.of(OrderTableFixture.create(true, 0)));
//
//            // when
//            // then
//            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        void 주문_테이블이_이미_주문을_받았거나_다른_단체_테이블에_등록된_경우_실패() {
//            // given
//            TableGroup firstTableGroup = TableGroupFixture.create(List.of(firstOrderTable, secondOrderTable));
//            TableGroup savedTableGroup = tableGroupService.create(firstTableGroup);
//
//            firstOrderTable.setTableGroupId(savedTableGroup.getId());
//            secondOrderTable.setTableGroupId(savedTableGroup.getId());
//
//            // when
//            final TableGroup newTableGroup = TableGroupFixture.create(List.of(firstOrderTable, secondOrderTable));
//
//            // then
//            assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//    }
//
//    @Nested
//    class 단체_테이블을_해제할_때 {
//
//        @Test
//        void success() {
//            // given
//            TableGroup tableGroup = TableGroupFixture.create(List.of(firstOrderTable, secondOrderTable));
//            TableGroup savedTableGroup = tableGroupService.create(tableGroup);
//
//            // when
//            tableGroupService.ungroup(savedTableGroup.getId());
//
//            // then
//            OrderTable ungroupedOrderTable = orderTableDao.findAll().stream()
//                    .filter(orderTable -> orderTable.getId().equals(firstOrderTable.getId()))
//                    .findFirst()
//                    .get();
//
//            assertThat(ungroupedOrderTable.getTableGroupId()).isNull();
//        }
//
//
//        @ParameterizedTest
//        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
//        void 주문_상태가_조리_중이거나_식사_중인_경우_실패(OrderStatus orderStatus) {
//            // given
//            TableGroup tableGroup = TableGroupFixture.create(List.of(firstOrderTable, secondOrderTable));
//            TableGroup createdGroup = tableGroupService.create(tableGroup);
//            Long orderTableId = createdGroup.getId();
//
//            orderRepository.save(OrderFixture.create(orderStatus, firstOrderTable.getId()));
//
//            // when
//            // then
//            assertThatThrownBy(() -> tableGroupService.ungroup(orderTableId))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//    }

}
