package kitchenpos.order_table.application;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderLineItem;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.dto.OrderTableRequest;
import kitchenpos.order_table.dto.OrderTableResponse;
import kitchenpos.order_table.repository.OrderTableDao;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class OrderTableServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 추가")
    @Test
    void create() {
        OrderTableRequest request = createTableRequest(0, true);

        OrderTableResponse savedTable = orderTableService.create(request);

        assertThat(savedTable.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        OrderTableRequest request = createTableRequest(0, true);
        orderTableService.create(request);
        orderTableService.create(request);

        List<OrderTableResponse> list = orderTableService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 등록 불가 여부 변경")
    @Test
    void changeEmpty() {
        OrderTableRequest table = createTableRequest(0, true);
        OrderTableResponse savedTable = orderTableService.create(table);
        OrderTableRequest request = createTableRequest(false);

        OrderTableResponse changedTable = orderTableService
            .changeEmpty(savedTable.getId(), request);

        assertThat(changedTable.isEmpty()).isEqualTo(request.getEmpty());
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_NotExistTable() {
        OrderTableRequest request = createTableRequest(false);

        assertThatThrownBy(() -> orderTableService.changeEmpty(1000L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 조리, 식사 중인 테이블의 주문 등록 불가 여부 변경")
    @Test
    @Transactional
    void changeEmpty_Fail_With_TableInProgress() {
        OrderTableRequest table = createTableRequest(0, false);
        OrderTableResponse orderTableResponse = orderTableService.create(table);

        OrderTable savedTable = orderTableDao.findById(orderTableResponse.getId()).get();

        Product product = createProduct(10_000);
        MenuProduct menuProduct = createMenuProduct(product, 2);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);
        Menu menu = createMenu(menuProducts,18_000);
        OrderLineItem orderLineItem = createOrderLineItem(menu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
        Order order = createOrder(savedTable, orderLineItems);
        orderDao.save(order);

        OrderTableRequest request = createTableRequest(true);

        assertThatThrownBy(() -> orderTableService.changeEmpty(savedTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTableRequest table = createTableRequest(0, false);
        OrderTableResponse savedTable = orderTableService.create(table);
        OrderTableRequest request = createTableRequest(10);

        OrderTableResponse changedTable = orderTableService
            .changeNumberOfGuests(savedTable.getId(), request);

        assertThat(changedTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_NotExistTable() {
        OrderTableRequest request = createTableRequest(100);

        assertThatThrownBy(
            () -> orderTableService.changeNumberOfGuests(1000L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTableRequest createTableRequest(int numberOfGuests, boolean empty) {
        return OrderTableRequest.builder()
            .numberOfGuests(numberOfGuests)
            .empty(empty)
            .build();
    }

    private OrderTableRequest createTableRequest(boolean empty) {
        return OrderTableRequest.builder()
            .numberOfGuests(null)
            .empty(empty)
            .build();
    }

    private OrderTableRequest createTableRequest(int numberOfGuests) {
        return OrderTableRequest.builder()
            .numberOfGuests(numberOfGuests)
            .empty(null)
            .build();
    }
}