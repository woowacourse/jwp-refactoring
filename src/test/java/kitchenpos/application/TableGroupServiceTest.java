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
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
            assertAll(
                    () -> assertThat(orderTableDao.findById(orderTable1.getId()).get().getTableGroupId()).isNull(),
                    () -> assertThat(orderTableDao.findById(orderTable1.getId()).get().isEmpty()).isFalse(),
                    () -> assertThat(orderTableDao.findById(orderTable2.getId()).get().getTableGroupId()).isNull(),
                    () -> assertThat(orderTableDao.findById(orderTable2.getId()).get().isEmpty()).isFalse()
            );
        }

        @Test
        @DisplayName("orderTable에 해당하는 order의 orderStatus가 COMPLETION이 아닌 경우 예외를 던진다.")
        void orderStatus_NotCompletion_ExceptionThrown() {
            // given
            OrderTable orderTable1 = saveOrderTable(2, true);
            OrderTable orderTable2 = saveOrderTable(4, true);
            TableGroup savedTableGroup = saveTableGroup(orderTable1, orderTable2);

            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            Order savedOrder = saveOrder(orderTable1, menu1, menu2);
            updateOrder(savedOrder, "COOKING");

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
