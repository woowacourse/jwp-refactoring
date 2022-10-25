package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fakedao.OrderFakeDao;
import kitchenpos.fakedao.OrderTableFakeDao;
import kitchenpos.fakedao.TableGroupFakeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TableGroupServiceTest {

    private OrderDao orderDao = new OrderFakeDao();
    private OrderTableDao orderTableDao = new OrderTableFakeDao();
    private TableGroupDao tableGroupDao = new TableGroupFakeDao();

    private TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    @DisplayName("단체 지정을 생성할 때")
    @Nested
    class Create {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(null, 1, true)));
            orderTables.add(orderTableDao.save(new OrderTable(null, 2, true)));
            orderTables.add(orderTableDao.save(new OrderTable(null, 3, true)));

            // when
            TableGroup actual = tableGroupService.create(new TableGroup(null, orderTables));

            // then
            Optional<TableGroup> tableGroup = tableGroupDao.findById(actual.getId());
            assertAll(
                    () -> assertThat(tableGroup).isPresent(),
                    () -> assertThat(tableGroup.get().getCreatedDate()).isNotNull(),
                    () -> assertThat(tableGroup.get().getOrderTables()).allMatch(orderTable -> !orderTable.isEmpty())
            );
        }

        @DisplayName("주문 테이블의 수가 2 미만이면 예외를 발생시킨다.")
        @Test
        void numberOfOrderTablesLessThanTwo_exception() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(null, 1, true)));

            // then
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup(null, orderTables)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("저장된 주문 테이블의 수와 맞지 않으면 예외를 발생시킨다.")
        @Test
        void notMatchNumberOfOrderTables_exception() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(null, 1, true)));
            orderTables.add(orderTableDao.save(new OrderTable(null, 2, true)));
            orderTables.add(new OrderTable(null, 3, true));

            // then
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup(null, orderTables)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("저장된 주문 테이블이 이미 있으면 예외를 발생시킨다.")
        @Test
        void savedOrderTable_exception() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(null, 1, true)));
            orderTables.add(orderTableDao.save(new OrderTable(null, 2, false)));

            // then
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup(null, orderTables)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 단체 지정이 된 주문 테이블이 있으면 예외를 발생시킨다.")
        @Test
        void alreadyOrderTableHasTableGroup_exception() {
            // given
            TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()));
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(tableGroup.getId(), 1, true)));
            orderTables.add(orderTableDao.save(new OrderTable(null, 2, false)));

            // then
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup(null, orderTables)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정된 주문 테이블들을 분리할 때")
    @Nested
    class Ungroup {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()));
            orderTableDao.save(new OrderTable(tableGroup.getId(), 1, true));
            orderTableDao.save(new OrderTable(tableGroup.getId(), 2, true));
            orderTableDao.save(new OrderTable(tableGroup.getId(), 3, true));

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            List<OrderTable> actual = orderTableDao.findAllByTableGroupId(tableGroup.getId());
            assertThat(actual).hasSize(0);
        }

        @DisplayName("주문 테이블들 중 하나라도 조리중이거나 조리가 됐다면 예외를 발생시킨다.")
        @Test
        void orderTablesStatusIsMealOrCooking_exception() {
            // given
            TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()));
            OrderTable orderTable1 = orderTableDao.save(new OrderTable(tableGroup.getId(), 1, true));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(tableGroup.getId(), 2, true));
            OrderTable orderTable3 = orderTableDao.save(new OrderTable(tableGroup.getId(), 3, true));

            orderDao.save(new Order(orderTable1.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), new ArrayList<>()));
            orderDao.save(new Order(orderTable2.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                    new ArrayList<>()));
            orderDao.save(new Order(orderTable3.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                    new ArrayList<>()));

            // then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
