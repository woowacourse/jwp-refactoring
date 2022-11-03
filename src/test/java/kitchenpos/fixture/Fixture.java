package kitchenpos.fixture;

import static kitchenpos.domain.order.OrderStatus.COOKING;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.request.MenuGroupCreateRequest;
import kitchenpos.application.request.OrderTableCreateRequest;
import kitchenpos.application.request.ProductCreateRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;

public class Fixture {

    public static final Product PRODUCT_후라이드 = new Product(1L, "후라이드",
            new ProductPrice(BigDecimal.valueOf(16000)));
    public static final Product PRODUCT_양념치킨 = new Product(2L, "양념치킨",
            new ProductPrice(BigDecimal.valueOf(17000)));

    public static final Menu MENU_후라이드치킨 = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 1L,
            List.of(new MenuProduct(PRODUCT_후라이드.getId(), 1L)));
    public static final Menu MENU_양념치킨 = new Menu(2L, "양념치킨", BigDecimal.valueOf(17000), 1L,
            List.of(new MenuProduct(PRODUCT_양념치킨.getId(), 1L)));

    public static final Order ORDER_첫번째_주문 = new Order(1L, 1L, COOKING, LocalDateTime.now(),
            List.of(new OrderLineItem(MENU_후라이드치킨.getId(), 1L)));

    public static final MenuGroupCreateRequest 한마리메뉴_생성요청 = new MenuGroupCreateRequest("한마리메뉴");
    public static final MenuGroupCreateRequest 두마리메뉴_생성요청 = new MenuGroupCreateRequest("두마리메뉴");

    public static final ProductCreateRequest 후라이드상품_생성요청 = new ProductCreateRequest("후라이드치킨",
            BigDecimal.valueOf(16000));
    public static final ProductCreateRequest 양념치킨상품_생성요청 = new ProductCreateRequest("양념치킨", BigDecimal.valueOf(16000));

    public static final OrderTableCreateRequest 삼인테이블_생성요청 = new OrderTableCreateRequest(3, true);
    public static final OrderTableCreateRequest 사인테이블_생성요청 = new OrderTableCreateRequest(4, true);
}
