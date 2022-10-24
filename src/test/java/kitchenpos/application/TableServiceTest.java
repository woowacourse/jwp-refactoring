package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        final var table = new OrderTable(2);

        final var result = tableService.create(table);

        assertThat(table).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(result);
    }

    @DisplayName("등록된 주문 테이블의 빈 테이블 여부 상태 변경")
    @Test
    void changeEmpty() {
        final var table = tableService.create(new OrderTable(2));

        final var updatedTable = new OrderTable(2);
        updatedTable.setEmpty(true);

        final var result = tableService.changeEmpty(table.getId(), updatedTable);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(table.getId()),
                () -> assertThat(result.isEmpty()).isEqualTo(updatedTable.isEmpty())
        );
    }

    @DisplayName("등록된 주문 테이블의 주문 상태가 COMPLETION이 아닐 경우, 빈 테이블 상태 변경 시 예외 발생")
    @Test
    void changeEmpty_orderStatusIsNotCompletion_throwsException() {

        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var pizzaMenu = menuDao.save(
                new Menu("싼 피자", new BigDecimal(9_000), italian.getId(), List.of(pizzaInMenu)));

        final var orderItem = new OrderLineItem(pizzaMenu.getId(), 1);

        final var table = tableService.create(new OrderTable(2));
        final var order = new Order(table.getId(), List.of(orderItem));
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.MEAL.name());
        orderDao.save(order);

        final var changed = new OrderTable(2);
        changed.setEmpty(true);

        assertThatThrownBy(
                () -> tableService.changeEmpty(table.getId(), changed)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블의 빈 테이블 여부 상태 변경 시 예외 발생")
    @Test
    void changeEmpty_hasTableGroup_throwsException() {
        final var table = new OrderTable(2);
        table.setTableGroupId(1L);
        tableService.create(table);

        final var updatedTable = new OrderTable(2);
        updatedTable.setEmpty(true);

        assertThatThrownBy(
                () -> tableService.changeEmpty(table.getId(), updatedTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("등록된 주문 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuests() {
        final var table = tableService.create(new OrderTable(2));

        final var updatedTable = new OrderTable(3);

        final var result = tableService.changeNumberOfGuests(table.getId(), updatedTable);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(table.getId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(updatedTable.getNumberOfGuests())
        );
    }

    @DisplayName("등록된 주문 테이블의 고객 수를 0 미만으로 변경 시 예외 발생")
    @Test
    void changeNumberOfGuests_toUnderZero_throwsException() {
        final var table = tableService.create(new OrderTable(2));

        final var updatedTable = new OrderTable(-1);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(table.getId(), updatedTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블의 상태가 빈 테이블 일 때, 고객 수 변경 시 예외 발생")
    @Test
    void changeNumberOfGuests_tableIsEmptyTrue_throwsException() {
        final var table = new OrderTable(2);
        table.setEmpty(true);
        tableService.create(table);

        final var updatedTable = new OrderTable(3);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(table.getId(), updatedTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 모든 주문 테이블 목록 조회")
    @Test
    void list() {
        final var existingTables = tableService.list();

        final var twoPeopleTable = new OrderTable(2);
        final var fivePeopleTable = new OrderTable(5);

        tableService.create(twoPeopleTable);
        tableService.create(fivePeopleTable);

        final var result = tableService.list();
        final var expected = List.of(twoPeopleTable, fivePeopleTable);

        assertThat(result.size()).isEqualTo(existingTables.size() + expected.size());
    }
}
