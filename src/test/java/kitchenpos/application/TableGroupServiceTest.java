package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ClassConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("TableGroupService의")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    TableGroupService tableGroupService;

    @MockBean
    OrderTableDao orderTableDao;

    @MockBean
    TableGroupDao tableGroupDao;

    @MockBean
    OrderDao orderDao;

    @Nested
    @DisplayName("create 메서드는")
    class Create {
        private static final long ORDER_TABLE_A_ID = 1L;
        private static final long ORDER_TABLE_B_ID = 2L;
        private static final long TABLE_GROUP_ID = 1L;

        private OrderTable orderTableA;
        private OrderTable orderTableB;
        private TableGroup tableGroup;
        private TableGroup savedTableGroup;

        @BeforeEach
        void setUp() {
            orderTableA = ClassConstructor.orderTable(ORDER_TABLE_A_ID, null, 0, true);
            orderTableB = ClassConstructor.orderTable(ORDER_TABLE_B_ID, null, 0, true);

            tableGroup = ClassConstructor.tableGroup(null, null, Arrays.asList(orderTableA, orderTableB));
            savedTableGroup = ClassConstructor.tableGroup(TABLE_GROUP_ID, LocalDateTime.now(),
                    Arrays.asList(orderTableA, orderTableB));

            given(orderTableDao.findAllByIdIn(any()))
                    .willReturn(Arrays.asList(orderTableA, orderTableB));
            given(tableGroupDao.save(tableGroup))
                    .willReturn(savedTableGroup);
        }

        @Test
        @DisplayName("테이블 그룹을 만들 수 있다.")
        void success() {
            //when
            TableGroup actual = tableGroupService.create(tableGroup);

            //then
            Assertions.assertAll(
                    () -> assertThat(actual.getOrderTables()).hasSize(2),
                    () -> assertThat(orderTableA.isEmpty()).isFalse(),
                    () -> assertThat(orderTableB.isEmpty()).isFalse()
            );
        }

        @Test
        @DisplayName("그룹으로 지정할 주문 테이블의 갯수가 2개보다 적으면, 예외를 던진다.")
        void fail_lessThanTwoTables() {
            //given
            tableGroup.setOrderTables(Arrays.asList(orderTableA));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹으로 지정할 주문 테이블이 존재하지 않으면, 예외를 던진다.")
        void fail_noExistTable() {
            //given
            given(orderTableDao.findAllByIdIn(any()))
                    .willReturn(Arrays.asList(orderTableB));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹으로 지정할 주문 테이블이 비어있지 않으면, 예외를 던진다.")
        void fail_notEmptyTable() {
            //given
            orderTableA.setEmpty(false);
            orderTableB.setEmpty(false);

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹으로 지정할 주문 테이블이 이미 테이블 그룹이 있으면, 예외를 던진다.")
        void fail_isAlreadyInGroup() {
            //given
            orderTableA.setTableGroupId(2L);
            orderTableB.setTableGroupId(2L);

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("ungroup 메서드는")
    class Ungroup {
        private static final long ORDER_TABLE_A_ID = 1L;
        private static final long ORDER_TABLE_B_ID = 2L;
        private static final long TABLE_GROUP_ID = 1L;

        private OrderTable orderTableA;
        private OrderTable orderTableB;
        private TableGroup tableGroup;

        @BeforeEach
        void setUp() {
            orderTableA = ClassConstructor.orderTable(ORDER_TABLE_A_ID, null, 0, true);
            orderTableB = ClassConstructor.orderTable(ORDER_TABLE_B_ID, null, 0, true);

            tableGroup = ClassConstructor.tableGroup(null, null, Arrays.asList(orderTableA, orderTableB));

            given(orderTableDao.findAllByTableGroupId(TABLE_GROUP_ID))
                    .willReturn(Arrays.asList(orderTableA, orderTableB));

            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                    Arrays.asList(ORDER_TABLE_A_ID, ORDER_TABLE_B_ID),
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                    .willReturn(false);
        }

        @Test
        void success() {
            //when
            tableGroupService.ungroup(TABLE_GROUP_ID);

            //then
            Assertions.assertAll(
                    () -> assertThat(orderTableA.isEmpty()).isFalse(),
                    () -> assertThat(orderTableA.getTableGroupId()).isNull(),
                    () -> assertThat(orderTableB.isEmpty()).isFalse(),
                    () -> assertThat(orderTableB.getTableGroupId()).isNull()
            );
        }

        @Test
        @DisplayName("그룹의 주문 테이블들 중 조리중이거나 식사중인 테이블이 존재하면, 예외를 던진다.")
        void fail_tableIsCookingOrMeal() {
            //given
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                    Arrays.asList(ORDER_TABLE_A_ID, ORDER_TABLE_B_ID),
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                    .willReturn(true);

            //when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(TABLE_GROUP_ID))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
