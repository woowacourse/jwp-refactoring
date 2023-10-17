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
        OrderTable 주문테이블 = tableService.create(orderTable);

        // when & then
        assertThat(주문테이블.isEmpty()).isEqualTo(true);

        주문테이블.setEmpty(false);
        OrderTable Empty_상태가_FALSE_변경된_주문테이블 = tableService.changeEmpty(주문테이블.getId(), 주문테이블);
        assertThat(Empty_상태가_FALSE_변경된_주문테이블.isEmpty()).isEqualTo(false);

        주문테이블.setEmpty(true);
        OrderTable Empty_상태가_TRUE_변경된_주문테이블 = tableService.changeEmpty(주문테이블.getId(), 주문테이블);
        assertThat(Empty_상태가_TRUE_변경된_주문테이블.isEmpty()).isEqualTo(true);
    }

    @DisplayName("테이블 empty 상태 변경 시, 주문 테이블을 찾을 수 없는 경우 예외가 발생한다.")
    @Test
    void changeEmpty_FailWithInvalidOrderTableId() {
        // given
        OrderTable 주문테이블 = tableService.create(orderTable);
        주문테이블.setEmpty(false);

        long invalidOrderTableId = 1000L;

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, 주문테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("테이블 empty 상태 변경 시, 테이블 그룹 ID가 null이 아닌 경우 예외가 발생한다.")
    @Test
    void changeEmpty_FailWithInvalidTableGroupId() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable));
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup 테이블그룹 = tableGroupDao.save(tableGroup);

        orderTable.setTableGroupId(테이블그룹.getId());
        OrderTable 주문테이블 = orderTableDao.save(orderTable);
        주문테이블.setEmpty(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), 주문테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 empty 상태 변경 시, 주문의 상태가 COOKING 또는 MEAL인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_FailWithInvalidOrderStatus(String invalidOrderStatus) {
        // given
        OrderTable 주문테이블 = orderTableDao.save(orderTable);
        주문테이블.setEmpty(false);

        Order order = new Order();
        order.setOrderStatus(invalidOrderStatus);
        order.setOrderTableId(주문테이블.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

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
    @ValueSource(ints = {-1, -100})
    void changeNumberOfGuests_FailWithInvalidNumberOfGuests(int invalidNumberOfGuests) {
        // given
        orderTable.setEmpty(false);
        OrderTable 주문테이블 = orderTableDao.save(orderTable);
        주문테이블.setNumberOfGuests(invalidNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 numberOfGuests 변경 시, orderTableId를 찾을 수 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_FailWithInvalidOrderTableId() {
        // given
        orderTable.setEmpty(false);
        OrderTable invalidOrderTable = orderTableDao.save(orderTable);
        invalidOrderTable.setNumberOfGuests(1);

        Long invalidOrderTableId = 100L;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, invalidOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 numberOfGuests 변경 시, 테이블이 비어있는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_FailWithEmptyIsTrue() {
        // given
        OrderTable 주문테이블 = orderTableDao.save(orderTable);
        주문테이블.setNumberOfGuests(1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
