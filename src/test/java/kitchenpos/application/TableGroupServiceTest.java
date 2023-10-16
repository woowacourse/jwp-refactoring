package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable savedOrderTable1;

    private OrderTable savedOrderTable2;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = createOrderTable(true, null, 10);
        OrderTable orderTable2 = createOrderTable(true, null, 15);
        savedOrderTable1 = tableDao.save(orderTable1);
        savedOrderTable2 = tableDao.save(orderTable2);
    }

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 정상_요청() {
            // given
            TableGroup tableGroup = createTableGroup(savedOrderTable1, savedOrderTable2);

            // when
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // then
            assertThat(savedTableGroup.getOrderTables())
                    .extracting(OrderTable::getId)
                    .contains(savedOrderTable1.getId(), savedOrderTable2.getId());
        }

        @Test
        void 요청의_주문_테이블이_2개_미만이면_예외_발생() {
            // given
            TableGroup tableGroup = createTableGroup(savedOrderTable1);

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(tableGroup)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청의_주문_테이블이_존재하지_않으면_예외_발생() {
            // given
            long invalidOrderTableId = -1;
            OrderTable orderTable = createOrderTable(true, null, 1);
            orderTable.setId(invalidOrderTableId);
            TableGroup tableGroup = createTableGroup(orderTable);

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(tableGroup)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청의_주문_테이블이_비어있지_않으면_예외_발생() {
            // given
            OrderTable orderTable = createOrderTable(false, null, 15);
            orderTable = tableDao.save(orderTable);
            TableGroup tableGroup = createTableGroup(orderTable);

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(tableGroup)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블에_그룹이_이미_존재하면_예외_발생() {
            // given
            TableGroup savedTableGroup = tableGroupDao.save(createTableGroup());
            OrderTable orderTable1 = createOrderTable(true, savedTableGroup.getId(), 10);
            OrderTable orderTable2 = createOrderTable(true, savedTableGroup.getId(), 15);
            OrderTable savedOrderTable1 = tableDao.save(orderTable1);
            OrderTable savedOrderTable2 = tableDao.save(orderTable2);
            TableGroup tableGroup = createTableGroup(savedOrderTable1, savedOrderTable2);

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(tableGroup)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_삭제 {

        @Test
        void 정상_요청() {
            // given
            TableGroup tableGroup = createTableGroup(savedOrderTable1, savedOrderTable2);
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            List<OrderTable> tables = tableDao.findAllByTableGroupId(savedTableGroup.getId());
            assertThat(tables).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COOKING"})
        void 요청에_대한_주문의_상태가_COOKING이나_MEAL이면_예외_발생(OrderStatus orderStatus) {
            // given
            TableGroup tableGroup = createTableGroup(savedOrderTable1, savedOrderTable2);
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);
            Order order = createOrder(savedOrderTable1.getId(), orderStatus);
            orderDao.save(order);

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.ungroup(savedTableGroup.getId())
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderTable createOrderTable(final boolean empty,
                                        final Long tableGroupId,
                                        final Integer numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    private TableGroup createTableGroup(final OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(orderTables));
        return tableGroup;
    }

    private Order createOrder(final Long orderTableId,
                              final OrderStatus status) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(status.name());
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }
}
