package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 성공() {
            // given
            OrderTable orderTable1 = orderTableDao.save(new OrderTable(2, true));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(3, true));
            TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

            // when
            TableGroup actual = tableGroupService.create(tableGroup);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getOrderTables()).hasSize(2)
            );
        }

        @Test
        void 주문_그룹이_비어있으면_예외() {
            // when && then
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup(Collections.emptyList())))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_그룹이_1개면_예외() {
            // given
            TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable(3, true)));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_주문_그룹과_주어진_주문_그룹_갯수가_다르면_예외() {
            // given
            OrderTable orderTable1 = orderTableDao.save(new OrderTable(4, true));
            OrderTable orderTable2 = new OrderTable(4, true);
            TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문테이블이_비어있지않으면_예외() {
            // given
            OrderTable orderTable1 = orderTableDao.save(new OrderTable(4, true));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(4, false));
            TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제 {


        @Test
        void 성공() {
            // given
            OrderTable orderTableA = orderTableDao.save(new OrderTable(3, true));
            OrderTable orderTableB = orderTableDao.save(new OrderTable(2, true));
            orderDao.save(
                new Order(orderTableA.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), Collections.emptyList()));
            orderDao.save(
                new Order(orderTableB.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), Collections.emptyList()));
            TableGroup tableGroup = tableGroupService.create(
                new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableA, orderTableB)));

            // when
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 해당하는_테이블의_주문이_예약중이거나_요리중이면_예외(OrderStatus orderStatus) {
            // given
            OrderTable orderTableA = orderTableDao.save(new OrderTable(3, true));
            OrderTable orderTableB = orderTableDao.save(new OrderTable(2, true));
            orderDao.save(
                new Order(orderTableA.getId(), orderStatus, LocalDateTime.now(), Collections.emptyList()));
            orderDao.save(
                new Order(orderTableB.getId(), orderStatus, LocalDateTime.now(), Collections.emptyList()));
            TableGroup tableGroup = tableGroupService.create(
                new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableA, orderTableB)));

            // when && then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
