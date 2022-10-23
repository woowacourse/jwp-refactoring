package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Nested
    @DisplayName("TableGroup을 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("OrderTable이 비어있으면 예외가 발생한다.")
        void orderTableEmptyFailed() {
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now())))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 주문 테이블입니다.");
        }

        @Test
        @DisplayName("OrderTable이 두 개보다 작을 경우 예외가 발생한다.")
        void orderTableUnderStandardFailed() {
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());
            tableGroup.addOrderTables(List.of(new OrderTable(5, true)));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 주문 테이블입니다.");
        }

        @Test
        @DisplayName("존재하지 않는 OrderTable일 경우 예외가 발생한다.")
        void orderTableNotExistFailed() {
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());
            tableGroup.addOrderTables(List.of(new OrderTable(5, true), new OrderTable(10, true)));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("주문 테이블이 비어있지 않을 경우 예외가 발생한다.")
        void orderTableNotEmptyFailed() {
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());

            OrderTable orderTable1 = orderTableDao.save(new OrderTable(5, false));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
            tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 이미 사용 중입니다.");
        }

        @Test
        @DisplayName("주문 테이블이 이미 그룹으로 할당되어 있으면 예외가 발생한다.")
        void orderTableAlreadyInTableGroup() {
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());

            TableGroup other = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
            OrderTable orderTable1 = orderTableDao.save(new OrderTable(other.getId(), 5, true));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
            tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 이미 사용 중입니다.");
        }

        @Test
        @DisplayName("테이블 그룹 생성에 성공한다.")
        void create() {
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());

            OrderTable orderTable1 = orderTableDao.save(new OrderTable(5, true));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
            tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

            TableGroup savedTableGroup = tableGroupService.create(tableGroup);
            assertThat(savedTableGroup).isEqualTo(tableGroupDao.findById(savedTableGroup.getId()).orElseThrow());
        }
    }

    @Nested
    @DisplayName("그룹을 해제할 때 ")
    class UngroupTest {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("주문 상태가 COMPLETION이 아닐 경우 실패한다.")
        void alreadyCookingFailed(final OrderStatus orderStatus) {
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());

            OrderTable orderTable1 = orderTableDao.save(new OrderTable(5, true));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
            tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            orderDao.save(new Order(orderTable1.getId(), orderStatus.name(), LocalDateTime.now()));
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 그룹을 분리할 수 없습니다.");
        }

        @Test
        @DisplayName("올바른 상태일 경우 성공한다.")
        void ungroup() {
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());

            OrderTable orderTable1 = orderTableDao.save(new OrderTable(5, true));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
            tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            orderDao.save(new Order(orderTable1.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now()));
            tableGroupService.ungroup(savedTableGroup.getId());

            assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).isEmpty();
        }
    }
}
