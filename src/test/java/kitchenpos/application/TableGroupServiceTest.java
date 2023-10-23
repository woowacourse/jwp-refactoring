package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
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
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    @DisplayName("테이블 그룹을 생성할 때 ")
    class Create {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void create() {
            // given
            final OrderTable orderTableA = new OrderTable(null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, 3, true);
            final OrderTable savedOrderTableA = orderTableRepository.save(orderTableA);
            final OrderTable savedOrderTableB = orderTableRepository.save(orderTableB);

            final List<OrderTable> orderTables = List.of(savedOrderTableA, savedOrderTableB);

            // when
            final TableGroup savedTableGroup = tableGroupService.create(orderTables);

            // then
            assertThat(savedTableGroup.getOrderTables()).hasSize(2);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("테이블 그룹의 주문 테이블이 빈 값이거나 컬렉션이 비어있는 경우 예외가 발생한다.")
        void throwExceptionWhenTableGroupIsNullOrEmpty(List<OrderTable> orderTables) {
            // given
            final OrderTable orderTableA = new OrderTable(null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, 3, true);
            orderTableRepository.save(orderTableA);
            orderTableRepository.save(orderTableB);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블 그룹의 주문 테이블의 개수가 2개보다 작은 경우 예외가 발생한다.")
        void throwExceptionWhenTableGroupCountIsUnderTwo() {
            // given
            final OrderTable orderTableA = new OrderTable(null, 2, true);
            orderTableRepository.save(orderTableA);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(List.of(orderTableA)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주어진 ID를 통해 찾은 주문 테이블의 수와 테이블 그룹에서의 주문 테이블 수가 다른 경우 예외가 발생한다.")
        void throwExceptionWhenTableGroupSizeDifferent() {
            // given
            final OrderTable orderTableA = new OrderTable(null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, 3, true);
            orderTableRepository.save(orderTableA);
            orderTableRepository.save(orderTableB);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(List.of(orderTableA)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 빈 테이블이 아닌 경우 예외가 발생한다.")
        void throwExceptionWhenOrderTableIsNotEmpty() {
            final OrderTable orderTableA = new OrderTable(null, 2, false);
            final OrderTable orderTableB = new OrderTable(null, 3, true);
            orderTableRepository.save(orderTableA);
            orderTableRepository.save(orderTableB);

            final List<OrderTable> orderTables = List.of(orderTableA, orderTableB);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 테이블 그룹 ID가 이미 있는 경우 예외가 발생한다.")
        void throwExceptionWhenTableGroupIdAlreadyExists() {
            final TableGroup beforeTableGroup = new TableGroup(new ArrayList<>());
            final OrderTable orderTableA = new OrderTable(beforeTableGroup, 2, true);
            final OrderTable orderTableB = new OrderTable(null, 3, true);

            tableGroupRepository.save(beforeTableGroup);

            final OrderTable savedOrderTableA = orderTableRepository.save(orderTableA);
            final OrderTable savedOrderTableB = orderTableRepository.save(orderTableB);

            final List<OrderTable> orderTables = List.of(savedOrderTableA, savedOrderTableB);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(orderTables))
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
            final OrderTable orderTableA = new OrderTable(null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, 3, true);
            final OrderTable savedOrderTableA = orderTableRepository.save(orderTableA);
            final OrderTable savedOrderTableB = orderTableRepository.save(orderTableB);

            final TableGroup savedTableGroup = tableGroupService.create(List.of(savedOrderTableA, savedOrderTableB));

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            final List<OrderTable> orderTables = orderTableRepository.findAll();
            final OrderTable ungroupedOrderTableA = orderTables.get(0);
            final OrderTable ungroupedOrderTableB = orderTables.get(1);

            assertAll(
                    () -> assertThat(ungroupedOrderTableA.getTableGroup()).isNull(),
                    () -> assertThat(ungroupedOrderTableA.isEmpty()).isFalse(),
                    () -> assertThat(ungroupedOrderTableB.getTableGroup()).isNull(),
                    () -> assertThat(ungroupedOrderTableB.isEmpty()).isFalse()
            );
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("주문 테이블에 해당하는 주문의 상태가 결제완료가 아니라면 예외가 발생한다.")
        void throwExceptionWhenOrderStatusIsNotCompletion(OrderStatus orderStatus) {
            // given
            final OrderTable orderTableA = new OrderTable(null, 2, true);
            final OrderTable orderTableB = new OrderTable(null, 3, true);
            final OrderTable savedOrderTableA = orderTableRepository.save(orderTableA);
            final OrderTable savedOrderTableB = orderTableRepository.save(orderTableB);

            final Order order = new Order(new ArrayList<>(), orderTableA, orderStatus);
            orderRepository.save(order);

            final TableGroup savedTableGroup = tableGroupService.create(List.of(savedOrderTableA, savedOrderTableB));

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
