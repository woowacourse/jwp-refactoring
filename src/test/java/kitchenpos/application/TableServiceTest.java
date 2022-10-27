package kitchenpos.application;

import static kitchenpos.fixture.MenuFactory.menu;
import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static kitchenpos.fixture.OrderTableFactory.emptyTable;
import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static kitchenpos.fixture.ProductFactory.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.OrderFactory;
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
        final var table = emptyTable(2);

        final var result = tableService.create(table);

        assertThat(table).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(result);
    }

    @DisplayName("등록된 주문 테이블의 빈 테이블 여부 상태 변경")
    @Test
    void changeEmpty() {
        final var table = tableService.create(notEmptyTable(2));

        final var updatedTable = notEmptyTable(2);
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

        final var pizza = productDao.save(product("피자", 10_000));
        final var italian = menuGroupDao.save(menuGroup("양식"));
        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));

        final var table = tableService.create(emptyTable(2));

        final var order = OrderFactory.order(table, pizzaMenu);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.MEAL.name());
        orderDao.save(order);

        final var changed = emptyTable(2);

        assertThatThrownBy(
                () -> tableService.changeEmpty(table.getId(), changed)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블의 빈 테이블 여부 상태 변경 시 예외 발생")
    @Test
    void changeEmpty_hasTableGroup_throwsException() {
        final var table = notEmptyTable(2);
        table.setTableGroupId(1L);
        tableService.create(table);

        final var updatedTable = emptyTable(2);
        updatedTable.setEmpty(true);

        assertThatThrownBy(
                () -> tableService.changeEmpty(table.getId(), updatedTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("등록된 주문 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuests() {
        final var table = tableService.create(notEmptyTable(2));

        final var updatedTable = notEmptyTable(3);

        final var result = tableService.changeNumberOfGuests(table.getId(), updatedTable);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(table.getId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(updatedTable.getNumberOfGuests())
        );
    }

    @DisplayName("등록된 주문 테이블의 고객 수를 0 미만으로 변경 시 예외 발생")
    @Test
    void changeNumberOfGuests_toUnderZero_throwsException() {
        final var table = tableService.create(notEmptyTable(2));

        final var updatedTable = notEmptyTable(-1);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(table.getId(), updatedTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블의 상태가 빈 테이블 일 때, 고객 수 변경 시 예외 발생")
    @Test
    void changeNumberOfGuests_tableIsEmptyTrue_throwsException() {
        final var table = emptyTable(2);
        tableService.create(table);

        final var updatedTable = emptyTable(3);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(table.getId(), updatedTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 모든 주문 테이블 목록 조회")
    @Test
    void list() {
        final var existingTables = tableService.list();

        final var twoPeopleTable = emptyTable(2);
        final var fivePeopleTable = emptyTable(5);

        tableService.create(twoPeopleTable);
        tableService.create(fivePeopleTable);

        final var result = tableService.list();
        final var expected = List.of(twoPeopleTable, fivePeopleTable);

        assertThat(result.size()).isEqualTo(existingTables.size() + expected.size());
    }
}
