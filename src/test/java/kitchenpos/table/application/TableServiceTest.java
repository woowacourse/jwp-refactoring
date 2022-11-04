package kitchenpos.table.application;

import static kitchenpos.common.fixture.OrderFixtures.generateOrder;
import static kitchenpos.common.fixture.OrderTableFixtures.*;
import static kitchenpos.common.fixture.TableGroupFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableSaveRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableServiceTest {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final TableService tableService;

    @Autowired
    public TableServiceTest(final OrderDao orderDao,
                            final OrderTableDao orderTableDao,
                            final TableGroupDao tableGroupDao,
                            final TableService tableService) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.tableService = tableService;
    }

    @Test
    void orderTable을_생성한다() {
        OrderTableSaveRequest 테이블_1번 = generateOrderTableSaveRequest(0, true);

        OrderTableResponse actual = tableService.create(테이블_1번);

        assertAll(() -> {
            assertThat(actual.getNumberOfGuests()).isEqualTo(테이블_1번.getNumberOfGuests());
            assertThat(actual.isEmpty()).isTrue();
        });
    }

    @Test
    void orderTable_list를_조회한다() {
        orderTableDao.save(generateOrderTable(0, true));
        orderTableDao.save(generateOrderTable(0, true));
        orderTableDao.save(generateOrderTable(0, true));
        orderTableDao.save(generateOrderTable(0, true));
        orderTableDao.save(generateOrderTable(0, true));

        List<OrderTableResponse> actual = tableService.list();

        assertThat(actual).hasSize(5);
    }

    @Test
    void orderTable의_empty를_변경한다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(null, 0, true));

        OrderTableResponse actual =
                tableService.changeEmpty(orderTable.getId(), new OrderTableChangeEmptyRequest(false));

        assertAll(() -> {
            assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            assertThat(actual.isEmpty()).isFalse();
        });
    }

    @Test
    void tableGroupId가_null이_아닌_경우_예외를_던진다() {
        TableGroup tableGroup = tableGroupDao.save(generateTableGroup(List.of()));
        OrderTable orderTable = orderTableDao.save(generateOrderTable(tableGroup.getId(), 0, true));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableChangeEmptyRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "orderTable에 속한 order 중 {0}이 존재하는 경우 예외를 던진다.")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void orderTable에_속한_order_중_COOKING_MEAL이_존재하는_경우_예외를_던진다(final OrderStatus orderStatus) {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(null, 0, true));

        orderDao.save(generateOrder(orderTable.getId(), orderStatus, List.of()));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableChangeEmptyRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTable의_changeNumberOfGuests를_변경한다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, false));

        OrderTableResponse actual =
                tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableChangeNumberOfGuestsRequest(1));

        assertAll(() -> {
            assertThat(actual.isEmpty()).isFalse();
            assertThat(actual.getNumberOfGuests()).isEqualTo(1);
        });
    }

    @ParameterizedTest(name = "numberOfGuests가 {0}미만인 경우 예외를 던진다")
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void numberOfGuests가_0미만인_경우_예외를_던진다(final int numberOfGuests) {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, false));

        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_orderTable이_비어있는_경우_예외를_던진다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, true));

        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
