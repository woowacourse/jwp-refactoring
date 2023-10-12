package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.test.fixtures.OrderFixtures;
import kitchenpos.test.fixtures.OrderTableFixtures;
import kitchenpos.test.fixtures.TableGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {
    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문 테이블을 생성한다")
    void createOrderTable() {
        // given
        final OrderTable orderTable = OrderTableFixtures.BASIC.get();

        // when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrderTable.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
            softly.assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        });
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void getOrderTables() {
        // given
        tableService.create(OrderTableFixtures.EMPTY.get());

        // when
        final List<OrderTable> actualOrderTables = tableService.list();

        // then
        assertThat(actualOrderTables).isNotEmpty();
    }

    @Nested
    @DisplayName("주문 테이블의 빈 테이블인지 여부를 수정할 시, ")
    class ChangeEmptyOrderTable {
        @Test
        @DisplayName("정상적으로 수정한다")
        void changeEmpty() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.EMPTY.get());
            savedOrderTable.setEmpty(false);

            // when
            final OrderTable updatedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);

            // then
            assertThat(updatedOrderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 시 예외 발생")
        void notExistOrderTableException() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.EMPTY.get());

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeEmpty(-1L, savedOrderTable));
        }

        @Test
        @DisplayName("테이블 그룹 아이디가 null이 아닐 시 예외 발생")
        void alreadyExistTableGroupException() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            final OrderTable firstOrderTable = tableService.create(OrderTableFixtures.BASIC.get());
            final OrderTable secondOrderTable = tableService.create(OrderTableFixtures.BASIC.get());

            final List<OrderTable> orderTables = tableGroup.getOrderTables();
            orderTables.get(0).setId(firstOrderTable.getId());
            orderTables.get(1).setId(secondOrderTable.getId());

            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.EMPTY.get());
            savedOrderTable.setTableGroupId(savedTableGroup.getId());
            orderTableDao.save(savedOrderTable);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable));
        }
    }
}
