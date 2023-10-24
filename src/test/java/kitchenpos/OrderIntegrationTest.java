package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderIntegrationTest extends IntegrationTest {

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        steps.createMenuGroup(MenuGroupFixture.LUNCH.toEntity());
        steps.createTable(OrderTableFixture.OCCUPIED_TABLE.toEntity());
        steps.createProduct(ProductFixture.FRIED_CHICKEN.toEntity());
        steps.createMenu(MenuFixture.LUNCH_SPECIAL.toEntity());
    }

    @Test
    void create_success() {
        // given
        Order expected = OrderFixture.ORDER_1.toEntity();

        // when
        steps.createOrder(expected);
        Order actual = sharedContext.getResponse().as(Order.class);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId()),
            () -> assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus()),
            () -> assertThat(actual.getOrderedTime()).isNotNull(),
            () -> assertThat(actual.getOrderLineItems()).isEqualTo(expected.getOrderLineItems())
        );
    }

    @Test
    void listOrders_success() {
        // given
        steps.createOrder(OrderFixture.ORDER_1.toEntity());

        // when
        List<Order> actual = RestAssured.given().log().all()
                                       .get("/api/orders")
                                       .then().log().all()
                                       .extract()
                                       .jsonPath().getList(".", Order.class);

        // then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void changeOrderStatus_success() {
        // given
        Order order = OrderFixture.ORDER_1.toEntity();
        steps.createOrder(order);

        // when
        order.setOrderStatus("COMPLETION");
        steps.changeOrderStatus(order.getId(), order);
        Order actual = sharedContext.getResponse().as(Order.class);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo("COMPLETION");
    }

    @Test
    void changeOrderStatus_failure() {
        // given
        Order order = OrderFixture.ORDER_1.toEntity();
        steps.createOrder(order);
        order.setOrderStatus("COMPLETION");
        steps.changeOrderStatus(order.getId(), order);

        // when
        steps.changeOrderStatus(order.getId(), order);
        var response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(500);
    }
}
