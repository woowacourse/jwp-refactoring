package kitchenpos.e2e;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.MenuFixture.createMenuRequest;
import static kitchenpos.support.MenuGroupFixture.단짜_두_마리_메뉴;
import static kitchenpos.support.ProductFixture.후라이드_치킨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public abstract class KitchenPosE2eTest extends E2eTest {

    public static final String PRODUCT_URL = "/api/products";
    public static final String MENU_URL = "/api/menus";
    public static final String MENU_GROUP_URL = "/api/menu-groups";
    public static final String ORDER_URL = "/api/orders";
    public static final String TABLE_URL = "/api/tables";
    public static final String TABLE_GROUP_URL = "/api/table-groups";
    public static final String TABLE_CHANGE_EMPTY_URL = "/api/tables/{orderTableId}/empty";
    public static final String TABLE_GROUP_DELETE_URL =  "/api/table-groups/{tableGroupId}";


    /**
     * -----------------------------
     * 생성 (API) 요청 편의 메서드 목록
     * -----------------------------
     */

    protected Order 주문_생성(final OrderStatus orderStatus) {

        final Long 메뉴_ID = 메뉴_생성_및_ID_반환();

        final Long 주문테이블_ID = POST_요청(TABLE_URL, new OrderTable(0, false)).as(OrderTable.class).getId();

        final LocalDateTime 주문일시 = now().minusMinutes(1);

        final Order 주문 = new Order(주문테이블_ID, orderStatus.name(), 주문일시, List.of(new OrderLineItem(메뉴_ID, 1)));

        return POST_요청(ORDER_URL, 주문).as(Order.class);
    }

    protected ExtractableResponse<Response> 주문_생성(final Long 주문테이블_ID, final LocalDateTime 주문일시) {

        final Long 메뉴_ID = 메뉴_생성_및_ID_반환();

        final Order 주문 = new Order(주문테이블_ID, MEAL.name(), 주문일시, List.of(new OrderLineItem(메뉴_ID, 1)));

        return POST_요청(ORDER_URL, 주문);
    }

    protected Order 주문_생성() {

        final Long 메뉴_ID = 메뉴_생성_및_ID_반환();

        final Long 주문테이블_ID = POST_요청(TABLE_URL, new OrderTable(0, false)).as(OrderTable.class).getId();

        final LocalDateTime 주문일시 = now().minusMinutes(1);

        final Order 주문 = new Order(주문테이블_ID, MEAL.name(), 주문일시, List.of(new OrderLineItem(메뉴_ID, 1)));

        return POST_요청(ORDER_URL, 주문).as(Order.class);
    }

    protected void 주문들_생성(final int size) {

        for (int i = 0; i < size; i++) {
            주문_생성();
        }
    }

    protected OrderTable 주문테이블_생성() {

        return 주문테이블_생성_변환(new OrderTable(0, true));
    }

    protected ExtractableResponse<Response> 주문테이블_생성(final OrderTable orderTable) {

        return POST_요청(TABLE_URL, orderTable);
    }

    protected OrderTable 주문테이블_생성_변환(final OrderTable orderTable) {

        return 주문테이블_생성(orderTable).body().as(OrderTable.class);
    }

    protected Long 주문테이블_생성_ID반환(final OrderTable orderTable) {

        return 주문테이블_생성(orderTable).body().as(OrderTable.class).getId();
    }

    protected List<OrderTable> 주문테이블들_생성(final int size) {

        return 주문테이블들_생성(size, 주문테이블_생성());
    }

    protected List<OrderTable> 주문테이블들_생성(final int size, final OrderTable orderTable) {

        List<OrderTable> orderTables = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            orderTables.add(주문테이블_생성_변환(orderTable));
        }

        return orderTables;
    }

    protected TableGroup 테이블그룹_생성(final TableGroup tableGroup) {

        final ExtractableResponse<Response> 응답 = POST_요청(TABLE_GROUP_URL, tableGroup);
        return 응답.body().as(TableGroup.class);
    }

    protected Long 메뉴_생성_및_ID_반환() {

        final Long 메뉴그룹_ID = 메뉴그룹_생성_및_ID_반환();
        final Long 후라이드치킨_ID = 상품_생성_및_ID_반환();
        final List<MenuProduct> 메뉴상품_리스트 = List.of(new MenuProduct(후라이드치킨_ID, 1));

        return POST_요청(MENU_URL, createMenuRequest("단 치킨 한 마리", 15_000, 메뉴그룹_ID, 메뉴상품_리스트))
                .as(Menu.class)
                .getId();
    }

    protected Long 메뉴_생성_및_ID_반환(final Long 메뉴그룹_ID, final List<MenuProduct> 메뉴상품_리스트) {

        return POST_요청(MENU_URL, createMenuRequest("단 치킨 한 마리", 15_000, 메뉴그룹_ID, 메뉴상품_리스트))
                .as(Menu.class)
                .getId();
    }

    protected Long 상품_생성_및_ID_반환() {

        return POST_요청(PRODUCT_URL, 후라이드_치킨)
                .as(Product.class)
                .getId();
    }

    protected Long 메뉴그룹_생성_및_ID_반환() {

        return POST_요청(MENU_GROUP_URL, 단짜_두_마리_메뉴)
                .as(MenuGroup.class)
                .getId();
    }
}
