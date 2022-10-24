package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;


    @DisplayName("테이블 그룹 등록")
    @Test
    void create() {
        final var singleTable = new OrderTable(1);
        singleTable.setEmpty(true);
        final var savedSingleTable = orderTableDao.save(singleTable);

        final var doubleTable = new OrderTable(2);
        doubleTable.setEmpty(true);
        final var savedDoubleTable = orderTableDao.save(doubleTable);

        final var tableGroup = new TableGroup(List.of(savedSingleTable, savedDoubleTable));

        final var result = tableGroupService.create(tableGroup);
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getOrderTables()).containsExactly(savedSingleTable, savedDoubleTable)
        );
    }

    @DisplayName("주문 테이블 목록이 비어있다면, 테이블 그룹 등록 시 예외 발생")
    @Test
    void create_emptyOrderTables_throwsException() {
        final var tableGroup = new TableGroup(Collections.emptyList());

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 하나 뿐이라면, 테이블 그룹 등록 시 예외 발생")
    @Test
    void create_onlyOneOrderTable_throwsException() {
        final var table = new OrderTable(1);
        table.setEmpty(true);
        final var savedTable = orderTableDao.save(table);

        final var tableGroup = new TableGroup(List.of(savedTable));

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 중 하나라도 빈 상태가 아니라면, 테이블 그룹 등록 시 예외 발생")
    @Test
    void create_containsNotEmptyTable_throwsException() {
        final var notEmptyTable = new OrderTable(2);
        notEmptyTable.setEmpty(false);
        final var savedNotEmptyTable = orderTableDao.save(notEmptyTable);

        final var emptyTable = new OrderTable(2);
        emptyTable.setEmpty(true);
        final var savedEmptyTable = orderTableDao.save(emptyTable);

        final var tableGroup = new TableGroup(List.of(savedNotEmptyTable, savedEmptyTable));

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 중 하나라도 이미 단체 id를 갖고 있다면, 테이블 그룹 등록 시 예외 발생")
    @Test
    void create_containsAlreadyGroupedTable_throwsException() {
        final var singleTable = new OrderTable(1);
        singleTable.setEmpty(true);
        final var savedSingleTable = orderTableDao.save(singleTable);

        final var coupleTable = new OrderTable(2);
        coupleTable.setEmpty(true);
        final var savedCoupleTable = orderTableDao.save(coupleTable);

        final var tripleTable = new OrderTable(3);
        tripleTable.setEmpty(true);
        final var savedTripleTable = orderTableDao.save(tripleTable);

        final var otherTableGroup = new TableGroup(List.of(savedSingleTable, savedCoupleTable));
        tableGroupService.create(otherTableGroup);

        final var tableGroup = new TableGroup(List.of(savedCoupleTable, savedTripleTable));

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        final var singleTable = new OrderTable(1);
        singleTable.setEmpty(true);
        final var savedSingleTable = orderTableDao.save(singleTable);

        final var doubleTable = new OrderTable(2);
        doubleTable.setEmpty(true);
        final var savedDoubleTable = orderTableDao.save(doubleTable);

        final var tableGroup = new TableGroup(List.of(savedSingleTable, savedDoubleTable));

        final var grouped = tableGroupService.create(tableGroup);
        tableGroupService.ungroup(grouped.getId());

        final var ungroupedTables = orderTableDao.findAllByIdIn(
                List.of(savedSingleTable.getId(), savedDoubleTable.getId()));
        assertAll(
                () -> assertThat(ungroupedTables).allMatch(table -> table.getTableGroupId() == null),
                () -> assertThat(ungroupedTables).allMatch(table -> !table.isEmpty())
        );
    }

    @DisplayName("그룹의 주문 테이블 중 하나라도 COMPLETION 상태가 아니라면 해제 시 예외 발생")
    @Test
    void ungroup_containsNotCompletionStatus_throwsException() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var coke = productDao.save(new Product("콜라", new BigDecimal(1_000)));
        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);
        final var cokeInMenu = new MenuProduct(coke.getId(), 2);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var pizzaMenu = menuDao.save(
                new Menu("싼 피자", new BigDecimal(9_000), italian.getId(), List.of(pizzaInMenu)));
        final var cokeMenu = menuDao.save(
                new Menu("싼 콜라", new BigDecimal(900), italian.getId(), List.of(cokeInMenu)));

        final var singleTable = new OrderTable(1);
        singleTable.setEmpty(true);
        final var savedSingleTable = orderTableDao.save(singleTable);

        final var doubleTable = new OrderTable(2);
        doubleTable.setEmpty(true);
        final var savedDoubleTable = orderTableDao.save(doubleTable);

        final var orderInMeal = new Order(savedDoubleTable.getId(), List.of(new OrderLineItem(pizzaMenu.getId(), 1)));
        orderInMeal.setOrderedTime(LocalDateTime.now());
        orderInMeal.setOrderStatus(OrderStatus.MEAL.name());
        orderDao.save(orderInMeal);

        final var orderInCompletion = new Order(savedDoubleTable.getId(), List.of(new OrderLineItem(cokeMenu.getId(), 1)));
        orderInCompletion.setOrderedTime(LocalDateTime.now());
        orderInCompletion.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(orderInCompletion);

        final var tableGroup = new TableGroup(List.of(savedSingleTable, savedDoubleTable));
        final var savedTableGroup = tableGroupService.create(tableGroup);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(savedTableGroup.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
