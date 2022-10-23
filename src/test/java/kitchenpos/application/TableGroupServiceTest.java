package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @DisplayName("테이블 그룹을 생성하는 기능")
    @Nested
    class CreateTest {

        @BeforeEach
        void setup() {
            orderTable1 = orderTableDao.save(new OrderTable(null, 0, true));
            orderTable2 = orderTableDao.save(new OrderTable(null, 0, true));
        }

        @DisplayName("정상 작동")
        @Test
        void create() {
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            assertThat(savedTableGroup.getId()).isNotNull();
        }

        @DisplayName("그룹화하려는 테이블 수가 2개 미만이면 예외가 발생한다.")
        @Test
        void create_Exception_NumOfTables() {
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("묶기를 요청한 테이블중 하나라도 실제로 존재하지 않는다면 예외가 발생한다.")
        @Test
        void create_Exception_NotExistOrderTable() {
            final OrderTable notSavedOrderTable = new OrderTable(null, 0, true);
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, notSavedOrderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 empty 상태가 아니면 예외가 발생한다.")
        @Test
        void create_Exception_NotEmptyStatus() {
            final OrderTable notEmptyOrderTable = orderTableDao.save(new OrderTable(null, 0, false));
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, notEmptyOrderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블의 테이블 그룹이 이미 존재한다면 예외가 발생한다.")
        @Test
        void create_Exception_AlreadyExist() {
            tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
    
    @DisplayName("테이블 그룹을 해제하는 기능")
    @Nested
    class UngroupTest {

        private TableGroup savedTableGroup;

        @BeforeEach
        void setup() {
            orderTable1 = orderTableDao.save(new OrderTable(null, 0, true));
            orderTable2 = orderTableDao.save(new OrderTable(null, 0, true));
            savedTableGroup = tableGroupService
                    .create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));
        }

        @DisplayName("정상 작동")
        @Test
        void ungroup() {
            tableGroupService.ungroup(savedTableGroup.getId());

            final OrderTable savedOrderTable = orderTableDao.findById(orderTable1.getId()).orElseThrow();
            assertThat(savedOrderTable.getTableGroupId()).isNull();
        }

        @DisplayName("테이블들의 주문들 중 조리, 식사 상태가 있는 경우 예외가 발생한다.")
        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void ungroup(OrderStatus orderStatus) {
            orderDao.save(new Order(orderTable1.getId(), orderStatus.name(), LocalDateTime.now(), Collections.emptyList()));

            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
