package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.createOrderTable;
import static kitchenpos.fixture.TableFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("TableGroupService 클래스의")
class TableGroupServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("orderTable 목록을 그룹화한다.")
        void success() {
            // given
            OrderTable orderTable1 = saveOrderTable(2, true);
            OrderTable orderTable2 = saveOrderTable(4, true);
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            // when
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // then
            Optional<TableGroup> actual = tableGroupDao.findById(savedTableGroup.getId());
            assertThat(actual).isPresent();
        }

        @Test
        @DisplayName("그룹화할 orderTable이 2개 미만인 경우 예외를 던진다.")
        void orderTableSize_SmallerThanTwo_ExceptionThrown() {
            // given
            OrderTable orderTable1 = saveOrderTable(2, true);
            TableGroup tableGroup = createTableGroup(orderTable1);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화할 orderTable이 존재하지 않는 경우 예외를 던진다.")
        void orderTable_NotExist_ExceptionThrown() {
            // given
            OrderTable orderTable1 = saveOrderTable(2, true);
            OrderTable orderTable2 = createOrderTable(4, true);
            TableGroup tableGroup = createTableGroup(orderTable1, orderTable2);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화할 orderTable이 빈 테이블이 아닌 경우 예외를 던진다.")
        void orderTable_NotEmpty_ExceptionThrown() {
            // given
            OrderTable orderTable1 = saveOrderTable(2, true);
            OrderTable orderTable2 = saveOrderTable(4, false);
            TableGroup tableGroup = createTableGroup(orderTable1, orderTable2);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화할 orderTable이 이미 그룹에 속한 경우 예외를 던진다.")
        void orderTable_alreadyGrouped_ExceptionThrown() {
            // given
            OrderTable orderTable1 = saveOrderTable(2, true);
            OrderTable orderTable2 = saveOrderTable(4, true);
            OrderTable orderTable3 = saveOrderTable(4, true);
            OrderTable orderTable4 = saveOrderTable(4, true);
            saveTableGroup(orderTable1, orderTable2, orderTable3, orderTable4);
            TableGroup tableGroup = createTableGroup(orderTable2, orderTable3, orderTable4);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("ungroup 메서드는")
    class Ungroup {

        @Test
        @DisplayName("그룹을 해제한다.")
        void success() {
            // given
            OrderTable orderTable1 = saveOrderTable(2, true);
            OrderTable orderTable2 = saveOrderTable(4, true);
            TableGroup savedTableGroup = saveTableGroup(orderTable1, orderTable2);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            Optional<OrderTable> actualOrderTable1 = orderTableDao.findById(orderTable1.getId());
            Optional<OrderTable> actualOrderTable2 = orderTableDao.findById(orderTable2.getId());
            assertThat(actualOrderTable1).isPresent();
            assertThat(actualOrderTable2).isPresent();
            assertAll(
                    () -> assertThat(actualOrderTable1.get().getTableGroupId()).isNull(),
                    () -> assertThat(actualOrderTable1.get().isEmpty()).isFalse(),
                    () -> assertThat(actualOrderTable2.get().getTableGroupId()).isNull(),
                    () -> assertThat(actualOrderTable2.get().isEmpty()).isFalse()
            );
        }

        @ParameterizedTest
        @EnumSource(
                value = OrderStatus.class,
                names = {"COMPLETION"},
                mode = EnumSource.Mode.EXCLUDE)
        @DisplayName("orderTable에 해당하는 order의 orderStatus가 COMPLETION이 아닌 경우 예외를 던진다.")
        void orderStatus_NotCompletion_ExceptionThrown(OrderStatus orderStatus) {
            // given
            OrderTable orderTable1 = saveOrderTable(2, true);
            OrderTable orderTable2 = saveOrderTable(4, true);
            TableGroup savedTableGroup = saveTableGroup(orderTable1, orderTable2);

            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            Order savedOrder = saveOrder(orderTable1, menu1, menu2);
            updateOrder(savedOrder, orderStatus.name());

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
