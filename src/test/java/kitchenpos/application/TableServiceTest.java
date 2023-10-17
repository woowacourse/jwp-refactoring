package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {


    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;


    @Test
    void 테이블_등록() {
        // given
        OrderTable orderTable = OrderTableFixture.create(true, 0);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(orderTableDao.findById(savedOrderTable.getId())).isPresent();
    }

    @Test
    void 테이블_목록_조회() {
        // given
        OrderTable orderTable1 = orderTableDao.save(OrderTableFixture.create(true, 0));
        OrderTable orderTable2 = orderTableDao.save(OrderTableFixture.create(true, 0));

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(orderTable1, orderTable2));
    }

    @Nested
    class 주문_테이블을_빈_테이블로_변경할_때 {

        @Test
        void success() {
            // given
            OrderTable savedOrderTable = orderTableDao.save(OrderTableFixture.create(true, 0));
            OrderTable updateOrderTable = OrderTableFixture.create(false, 0);

            // when
            OrderTable actual = tableService.changeEmpty(savedOrderTable.getId(), updateOrderTable);

            // then
            assertThat(actual.isEmpty()).isFalse();
        }

        @Test
        void 단체_테이블에_소속되어_있으면_실패() {
            // given
            OrderTable orderTable = orderTableDao.save(OrderTableFixture.create(true, 0));
            List<OrderTable> orderTables = List.of(
                    orderTable,
                    orderTableDao.save(OrderTableFixture.create(true, 0)),
                    orderTableDao.save(OrderTableFixture.create(true, 0))
            );
            TableGroup tableGroup = tableGroupService.create(TableGroupFixture.create(orderTables));

            orderTable.setTableGroupId(tableGroup.getId());
            OrderTable savedOrderTable = orderTableDao.save(orderTable);
            Long orderTableId = savedOrderTable.getId();
            OrderTable updateOrderTable = OrderTableFixture.create(false, 0);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, updateOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_조리중이거나_식사_중이면_실패() {
            // given
            OrderTable orderTable = orderTableDao.save(OrderTableFixture.create(false, 2));
            Long orderTableId = orderTable.getId();

            Order order = new Order();
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderTableId(orderTable.getId());
            order.setOrderedTime(LocalDateTime.now());

            orderDao.save(order);

            OrderTable updateOrderTable = new OrderTable();
            updateOrderTable.setEmpty(true);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, updateOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }


    }

    @Nested
    class 주문_테이블의_고객_수를_변경할_때 {


        @Test
        void success() {
            // given
            OrderTable orderTable = OrderTableFixture.create(false, 0);
            OrderTable savedTable = tableService.create(orderTable);

            OrderTable updateTable = new OrderTable();
            updateTable.setNumberOfGuests(4);

            // when
            OrderTable result = tableService.changeNumberOfGuests(savedTable.getId(), updateTable);

            // then
            assertThat(result.getNumberOfGuests()).isEqualTo(4);
        }

        @Test
        void 변경할_고객의_수가_0_미만이면_실패() {
            // given
            OrderTable orderTable = OrderTableFixture.create(false, 0);
            OrderTable savedTable = tableService.create(orderTable);
            Long orderTableId = savedTable.getId();

            OrderTable updateTable = new OrderTable();
            updateTable.setNumberOfGuests(-1);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, updateTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 등록되지_않은_주문_테이블이면_실패() {
            // given
            OrderTable orderTable = OrderTableFixture.create(false, 0);
            Long orderTableId = orderTable.getId();

            OrderTable updateTable = new OrderTable();
            updateTable.setNumberOfGuests(3);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, updateTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_빈_테이블이면_실패() {
            // given
            OrderTable orderTable = OrderTableFixture.create(true, 0);
            OrderTable savedTable = tableService.create(orderTable);
            Long orderTableId = savedTable.getId();

            OrderTable updateTable = new OrderTable();
            updateTable.setNumberOfGuests(3);

            // when
            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, updateTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }


}
