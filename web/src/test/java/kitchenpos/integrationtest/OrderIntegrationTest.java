package kitchenpos.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import java.util.List;
import kitchenpos.service.OrderDto;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderIntegrationTest extends IntegrationTest {

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        steps.createMenuGroup(MenuGroupFixture.LUNCH.toDto());
        steps.createTable(OrderTableFixture.OCCUPIED_TABLE.toDto());
        steps.createProduct(ProductFixture.FRIED_CHICKEN.toDto());
        steps.createMenu(MenuFixture.LUNCH_SPECIAL.toDto());
    }

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create_success() {
        // given
        OrderDto expected = OrderFixture.ORDER_1.toDto();

        // when
        steps.createOrder(expected);
        OrderDto actual = sharedContext.getResponse().as(OrderDto.class);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId()),
            () -> assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus()),
            () -> assertThat(actual.getOrderedTime()).isNotNull(),
            () -> assertThat(actual.getOrderLineItemDtos()).isEqualTo(expected.getOrderLineItemDtos())
        );
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다.")
    void listOrders_success() {
        // given
        steps.createOrder(OrderFixture.ORDER_1.toDto());

        // when
        List<OrderDto> actual = RestAssured.given().log().all()
                                           .get("/api/orders")
                                           .then().log().all()
                                           .extract()
                                           .jsonPath().getList(".", OrderDto.class);

        // then
        assertThat(actual).isNotEmpty();
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus_success() {
        // given
        OrderDto orderDto = OrderFixture.ORDER_1.toDto();
        steps.createOrder(orderDto);

        // when
        orderDto.setOrderStatus("COMPLETION");
        steps.changeOrderStatus(orderDto.getId(), orderDto);
        OrderDto actual = sharedContext.getResponse().as(OrderDto.class);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo("COMPLETION");
    }

    @Test
    @DisplayName("주문 상태가 이미 Completion이면 변경할 수 없다.")
    void changeOrderStatus_failure() {
        // given
        OrderDto orderDto = OrderFixture.ORDER_1.toDto();
        steps.createOrder(orderDto);
        orderDto.setOrderStatus("COMPLETION");
        steps.changeOrderStatus(orderDto.getId(), orderDto);

        // when
        steps.changeOrderStatus(orderDto.getId(), orderDto);
        var response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(500);
    }
}
