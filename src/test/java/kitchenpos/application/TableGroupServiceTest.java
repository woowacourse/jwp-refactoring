package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Nested
    @DisplayName("테이블 그룹을 생성할 때 ")
    class Create {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void create() {
            // given
            final OrderTable orderTableA = new OrderTable(null, null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, null, 3, true);
            final OrderTable savedOrderTableA = orderTableDao.save(orderTableA);
            final OrderTable savedOrderTableB = orderTableDao.save(orderTableB);

            final TableGroup tableGroup =
                    new TableGroup(null, LocalDateTime.now(), List.of(savedOrderTableA, savedOrderTableB));

            // when
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // then
            assertThat(savedTableGroup.getOrderTables()).hasSize(2);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("테이블 그룹의 주문 테이블이 빈 값이거나 컬렉션이 비어있는 경우 예외가 발생한다.")
        void throwExceptionWhenTableGroupIsNullOrEmpty(List<OrderTable> orderTables) {
            // given
            final OrderTable orderTableA = new OrderTable(null, null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, null, 3, true);
            orderTableDao.save(orderTableA);
            orderTableDao.save(orderTableB);

            final TableGroup tableGroup =
                    new TableGroup(null, LocalDateTime.now(), orderTables);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블 그룹의 주문 테이블의 개수가 2개보다 작은 경우 예외가 발생한다.")
        void throwExceptionWhenTableGroupCountIsUnderTwo() {
            // given
            final OrderTable orderTableA = new OrderTable(null, null, 2, true);
            orderTableDao.save(orderTableA);

            final TableGroup tableGroup =
                    new TableGroup(null, LocalDateTime.now(), List.of(orderTableA));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주어진 ID를 통해 찾은 주문 테이블의 수와 테이블 그룹에서의 주문 테이블 수가 다른 경우 예외가 발생한다.")
        void throwExceptionWhenTableGroupSizeDifferent() {
            // given
            final OrderTable orderTableA = new OrderTable(null, null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, null, 3, true);
            final OrderTable savedOrderTableA = orderTableDao.save(orderTableA);
            orderTableDao.save(orderTableB);

            final TableGroup tableGroup =
                    new TableGroup(null, LocalDateTime.now(), List.of(savedOrderTableA, orderTableB));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 빈 테이블이 아닌 경우 예외가 발생한다.")
        void throwExceptionWhenOrderTableIsNotEmpty() {
            final OrderTable orderTableA = new OrderTable(null, null, 2, false);
            final OrderTable orderTableB = new OrderTable(null, null, 3, true);
            final OrderTable savedOrderTableA = orderTableDao.save(orderTableA);
            final OrderTable savedOrderTableB = orderTableDao.save(orderTableB);

            final TableGroup tableGroup =
                    new TableGroup(null, LocalDateTime.now(), List.of(savedOrderTableA, savedOrderTableB));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 테이블 그룹 ID가 이미 있는 경우 예외가 발생한다.")
        void throwExceptionWhenTableGroupIdAlreadyExists() {
            final OrderTable orderTableA = new OrderTable(null, 1L, 2, true);
            final OrderTable orderTableB = new OrderTable(null, null, 3, true);

            final TableGroup beforeTableGroup = new TableGroup(null, LocalDateTime.now(), List.of(orderTableA));
            tableGroupDao.save(beforeTableGroup);

            final OrderTable savedOrderTableA = orderTableDao.save(orderTableA);
            final OrderTable savedOrderTableB = orderTableDao.save(orderTableB);

            final TableGroup tableGroup =
                    new TableGroup(null, LocalDateTime.now(), List.of(savedOrderTableA, savedOrderTableB));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("테이블 그룹을 해체할 때 ")
    class UnGroup {

        @Test
        @DisplayName("정상적으로 해체된다.")
        void ungroup() {
            // given
            final OrderTable orderTableA = new OrderTable(null, null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, null, 3, true);
            final OrderTable savedOrderTableA = orderTableDao.save(orderTableA);
            final OrderTable savedOrderTableB = orderTableDao.save(orderTableB);

            final TableGroup tableGroup =
                    new TableGroup(null, LocalDateTime.now(), List.of(savedOrderTableA, savedOrderTableB));
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            final List<OrderTable> orderTables = orderTableDao.findAll();
            final OrderTable ungroupedOrderTableA = orderTables.get(0);
            final OrderTable ungroupedOrderTableB = orderTables.get(1);

            assertAll(
                    () -> assertThat(ungroupedOrderTableA.getTableGroupId()).isNull(),
                    () -> assertThat(ungroupedOrderTableA.isEmpty()).isFalse(),
                    () -> assertThat(ungroupedOrderTableB.getTableGroupId()).isNull(),
                    () -> assertThat(ungroupedOrderTableB.isEmpty()).isFalse()
            );
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("주문 테이블에 해당하는 주문의 상태가 결제완료가 아니라면 예외가 발생한다.")
        void throwExceptionWhenOrderStatusIsNotCompletion(OrderStatus orderStatus) {
            // given
            final OrderTable orderTableA = new OrderTable(null, null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, null, 3, true);
            final OrderTable savedOrderTableA = orderTableDao.save(orderTableA);
            final OrderTable savedOrderTableB = orderTableDao.save(orderTableB);

            final Order order = new Order(null, 1L, orderStatus.name(), LocalDateTime.now(), null);
            orderDao.save(order);

            final TableGroup tableGroup =
                    new TableGroup(null, LocalDateTime.now(), List.of(savedOrderTableA, savedOrderTableB));
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
