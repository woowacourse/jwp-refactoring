package kitchenpos.application;

import static kitchenpos.application.fixture.OrderFixtures.generateOrder;
import static kitchenpos.application.fixture.OrderTableFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.fake.FakeOrderDao;
import kitchenpos.dao.fake.FakeOrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableServiceTest {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableService tableService;

    @Autowired
    private TableServiceTest() {
        this.orderDao = new FakeOrderDao();
        this.orderTableDao = new FakeOrderTableDao();
        this.tableService = new TableService(orderDao, orderTableDao);
    }

    @BeforeEach
    void setUp() {
        FakeOrderDao.deleteAll();
        FakeOrderTableDao.deleteAll();
    }

    @Test
    void orderTable을_생성한다() {
        OrderTable 테이블_1번 = 테이블_1번();

        OrderTable actual = tableService.create(테이블_1번);

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

        List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(5);
    }

    @Test
    void orderTable의_empty를_변경한다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(null, 0, true));

        OrderTable actual = tableService.changeEmpty(orderTable.getId(), generateOrderTable(null, 0, false));

        assertAll(() -> {
            assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            assertThat(actual.isEmpty()).isFalse();
        });
    }

    @Test
    void tableGroupId가_null이_아닌_경우_예외를_던진다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0L, 0, true));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), generateOrderTable(0, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "orderTable에 속한 order 중 {0}이 존재하는 경우 예외를 던진다.")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void orderTable에_속한_order_중_COOKING_MEAL이_존재하는_경우_예외를_던진다(final OrderStatus orderStatus) {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(null, 0, true));

        orderDao.save(generateOrder(orderTable.getId(), orderStatus, List.of()));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), generateOrderTable(0, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTable의_changeNumberOfGuests를_변경한다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, false));

        OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), generateOrderTable(1, false));

        assertAll(() -> {
            assertThat(actual.isEmpty()).isFalse();
            assertThat(actual.getNumberOfGuests()).isEqualTo(1);
        });
    }

    @ParameterizedTest(name = "numberOfGuests가 {0}미만인 경우 예외를 던진다")
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void numberOfGuests가_0미만인_경우_예외를_던진다(final int numberOfGuests) {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, false));

        OrderTable actual = generateOrderTable(numberOfGuests, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_orderTable이_비어있는_경우_예외를_던진다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, true));

        OrderTable actual = generateOrderTable(2, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
