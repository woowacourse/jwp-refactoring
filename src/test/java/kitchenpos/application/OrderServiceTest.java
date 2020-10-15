package kitchenpos.application;

import kitchenpos.application.common.MenuFixtureFactory;
import kitchenpos.application.common.TestObjectFactory;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/delete_all.sql")
class OrderServiceTest extends MenuFixtureFactory {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @DisplayName("주문 생성 메서드 테스트")
    @Test
    void create() {
        Order savedOrder = orderService.create(createOrderToSave());

        List<OrderLineItem> savedOrderLineItems = savedOrder.getOrderLineItems();
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrderLineItems.get(0).getOrderId()).isEqualTo(savedOrder.getId())
        );
    }

    @DisplayName("주문 생성 메서드 - 테이블이 빈 테이블인 경우 예외 처리")
    @Test
    void createWhenEmptyTable() {
        OrderTable orderTable = TestObjectFactory.creatOrderTableDto();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        List<OrderLineItem> orderLineItems = Arrays.asList(TestObjectFactory.createOrderLineItem(1, 1));
        Order orderRequest = TestObjectFactory.createOrderDto(savedOrderTable.getId(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 메서드 - OrderLineItems가 빈 경우 예외 처리")
    @Test
    void createWhenEmptyOrderLineItems() {
        OrderTable orderTable = TestObjectFactory.creatOrderTableDto();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order orderRequest = TestObjectFactory.createOrderDto(savedOrderTable.getId(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        orderService.create(createOrderToSave());
        orderService.create(createOrderToSave());

        assertThat(orderService.list()).hasSize(2);
    }

    @DisplayName("주문 상태 변경 메서드 테스트")
    @Test
    void changeOrderStatus() {
        Order savedOrder = orderService.create(createOrderToSave());
        Order changeOrderStatusDto = TestObjectFactory.createChangeOrderStatusDto(OrderStatus.MEAL.name());

        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusDto);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태 변경 - 이미 완료 상태인 경우 예외 처리")
    @Test
    void changeOrderStatusWhenCompletion() {
        Order savedOrder = orderService.create(createOrderToSave());
        Order changeOrderStatusDtoToCompletion = TestObjectFactory.createChangeOrderStatusDto(OrderStatus.COMPLETION.name());

        orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusDtoToCompletion);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusDtoToCompletion))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order createOrderToSave() {
        OrderTable orderTable = TestObjectFactory.creatOrderTableDto();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Menu savedMenu1 = menuDao.save(makeMenuToSave("추천메뉴", "양념", 12000));
        Menu savedMenu2 = menuDao.save(makeMenuToSave("추천메뉴", "후라이드", 11000));

        List<OrderLineItem> orderLineItems = Arrays.asList(
                TestObjectFactory.createOrderLineItem(savedMenu1.getId(), 1),
                TestObjectFactory.createOrderLineItem(savedMenu2.getId(), 1)
        );

        return TestObjectFactory.createOrderDto(savedOrderTable.getId(), orderLineItems);
    }
}
