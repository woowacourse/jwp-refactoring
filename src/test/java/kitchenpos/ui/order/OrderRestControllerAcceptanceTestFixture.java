package kitchenpos.ui.order;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.ui.dto.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_10개_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성_요청;
import static kitchenpos.fixture.OrderLineItemFixture.주문_품목_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class OrderRestControllerAcceptanceTestFixture extends IntegrationTestHelper {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuService menuService;

    protected MenuGroup menuGroup;
    protected Menu menu;
    protected OrderTable orderTable;
    protected OrderLineItem orderLineItem;
    protected Product product;
    protected MenuProduct menuProduct;

    @BeforeEach
    void initAcceptanceData() {
        menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
        product = productRepository.save(상품_생성_10000원());
        MenuCreateRequest req = new MenuCreateRequest("메뉴", 10000L, menuGroup.getId(), List.of(
                new MenuProductCreateRequest(product.getId(), 1)
        ));
        menu = menuService.create(req);
        menuProduct = menuProductRepository.save(메뉴_상품_10개_생성(product));

        orderTable = orderTableRepository.save(주문_테이블_생성(null, 1, false));
        orderLineItem = 주문_품목_생성(menu, 1L);
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

    protected Order 주문_데이터를_생성한다() {
        OrderCreateRequest req = 주문_생성_요청(orderTable, List.of(orderLineItem));

        return orderService.create(req);
    }

    protected ExtractableResponse 주문을_전체_조회한다(final String url) {
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
            softly.assertThat(result.getOrderTableId()).isEqualTo(order.getOrderTable().getId());
        });
    }

    protected void 주문들이_성공적으로_조회된다(final ExtractableResponse response, final Order order) {
        List<OrderResponse> result = response.jsonPath()
                .getList("", OrderResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
            softly.assertThat(result.get(0).getOrderTableId()).isEqualTo(order.getOrderTable().getId());
        });
    }

    protected void 주문이_성공적으로_변경된다(final ExtractableResponse response, final Order order) {
        OrderResponse result = response.as(OrderResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.getOrderStatus()).isNotEqualTo(order.getOrderStatus());
            softly.assertThat(result.getOrderTableId()).isEqualTo(order.getOrderTable().getId());
        });
    }
}
