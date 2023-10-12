package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import({
        TableService.class,
        JdbcTemplateOrderDao.class,
        JdbcTemplateOrderTableDao.class,
        JdbcTemplateTableGroupDao.class
})
class TableServiceTest extends ServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
    }

    @DisplayName("테이블을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        OrderTable expected = new OrderTable();
        expected.setEmpty(true);
        expected.setNumberOfGuests(0);

        // when
        OrderTable actual = tableService.create(expected);

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTableDao.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
            softly.assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
        });
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(0);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setNumberOfGuests(0);

        OrderTable 주문테이블1 = tableService.create(orderTable1);
        OrderTable 주문테이블2 = tableService.create(orderTable2);

        // when
        List<OrderTable> list = tableService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list).usingRecursiveComparison()
                    .isEqualTo(List.of(주문테이블1, 주문테이블2));
        });
    }

    @DisplayName("테이블의 empty 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable expected = tableService.create(orderTable);

        expected.setEmpty(false);

        // when
        OrderTable actual = tableService.changeEmpty(expected.getId(), expected);

        // then
        assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
    }

    @DisplayName("")
    @Test
    void changeEmpty_FailWithInvalidTableGroupId() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable));
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup 테이블그룹 = tableGroupDao.save(tableGroup);

        orderTable.setTableGroupId(테이블그룹.getId());

        OrderTable expected = orderTableDao.save(orderTable);

        expected.setEmpty(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(expected.getId(), expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_FailWithInvalidOrderStatus(String invalidOrderStatus) {
        // given
        OrderTable 주문테이블 = orderTableDao.save(orderTable);

        Order order = new Order();
        order.setOrderStatus(invalidOrderStatus);
        order.setOrderTableId(주문테이블.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        주문테이블.setEmpty(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), 주문테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 numberOfGuests를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        orderTable.setEmpty(false);

        OrderTable expected = orderTableDao.save(orderTable);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(expected.getId(), expected);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
    }

    @DisplayName("테이블의 numberOfGuests 변경 시, numberOfGuests가 음수인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -100 })
    void changeNumberOfGuests_FailWithInvalidNumberOfGuests(int invalidNumberOfGuests) {
        // given
        OrderTable expected = orderTableDao.save(orderTable);
        expected.setNumberOfGuests(invalidNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 numberOfGuests 변경 시, orderTableId를 찾을 수 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_FailWithInvalidOrderTableId() {
        // given
        OrderTable expected = orderTableDao.save(orderTable);

        expected.setNumberOfGuests(1);

        Long invalidOrderTableId = 100L;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 numberOfGuests 변경 시, 테이블이 비어있는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_FailWithEmptyIsTrue() {
        // given
        OrderTable expected = orderTableDao.save(orderTable);

        expected.setNumberOfGuests(1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), expected))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
