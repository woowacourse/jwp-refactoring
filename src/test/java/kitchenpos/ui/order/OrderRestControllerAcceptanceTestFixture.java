package kitchenpos.ui.order;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.order.dto.OrderCreateRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.ui.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성_요청;
import static kitchenpos.fixture.OrderLineItemFixture.주문_품목_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderRestControllerAcceptanceTestFixture extends IntegrationTestHelper {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    protected MenuGroup menuGroup;
    protected Menu menu;
    protected OrderTable orderTable;
    protected OrderLineItem orderLineItem;

    @BeforeEach
    void initAcceptanceData() {
        menuGroup = menuGroupDao.save(메뉴그룹_생성("그룹"));
        menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1000), menuGroup.getId(), null));
        orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
        orderLineItem = 주문_품목_생성(null, menu.getId(), 1);
    }

    protected <T> ExtractableResponse 주문_생성한다(final String url, final T request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    protected <T> ExtractableResponse 주문을_전체_조회한다(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    protected <T> ExtractableResponse 주문_상태를_변경한다(final String url, final T request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }

    protected void 주문이_성공적으로_생성된다(final ExtractableResponse response, final Order order) {
        OrderResponse result = response.as(OrderResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.getOrderStatus()).isEqualTo(COOKING.name());
            softly.assertThat(result.getOrderTableId()).isEqualTo(order.getOrderTableId());
        });
    }

    protected void 주문들이_성공적으로_생성된다(final ExtractableResponse response, final Order order) {
        List<OrderResponse> result = response.jsonPath()
                .getList("", OrderResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
            softly.assertThat(result.get(0).getOrderTableId()).isEqualTo(order.getOrderTableId());
        });
    }

    protected void 주문이_성공적으로_변경된다(final ExtractableResponse response, final Order order) {
        OrderResponse result = response.as(OrderResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.getOrderStatus()).isNotEqualTo(order.getOrderStatus());
            softly.assertThat(result.getOrderTableId()).isEqualTo(order.getOrderTableId());
        });
    }

    protected Order 주문_데이터를_생성한다() {
        Order order = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem));
        OrderCreateRequest req = 주문_생성_요청(order);

        return orderService.create(req);
    }
}
