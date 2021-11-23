package kitchenpos;

import kitchenpos.fixture.*;
import kitchenpos.testtool.RequestBuilder;
import kitchenpos.testtool.request.RequestApi;
import kitchenpos.testtool.response.HttpResponse;
import kitchenpos.ui.dto.request.*;
import kitchenpos.ui.dto.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AcceptanceTest {

    @Autowired
    private RequestBuilder request;

    @Autowired
    protected MenuFixture menuFixture;
    @Autowired
    protected OrderFixture orderFixture;
    @Autowired
    protected OrderLineItemFixture orderLineItemFixture;
    @Autowired
    protected OrderTableFixture orderTableFixture;
    @Autowired
    protected ProductFixture productFixture;
    @Autowired
    protected TableGroupFixture tableGroupFixture;

    protected final MenuGroupFixture menuGroupFixture = new MenuGroupFixture();
    protected final MenuProductFixture menuProductFixture = new MenuProductFixture();

    protected RequestApi request() {
        return request.builder();
    }

    protected MenuGroupResponse 메뉴그룹_등록(MenuGroupRequest request) {
        return request()
                .post("/api/menu-groups", request)
                .build()
                .convertBody(MenuGroupResponse.class);
    }

    protected List<MenuGroupResponse> 메뉴그룹_리스트_조회() {
        return request()
                .get("/api/menu-groups")
                .build()
                .convertBodyToList(MenuGroupResponse.class);
    }

    protected ProductResponse 상품_등록(ProductRequest request) {
        return request()
                .post("/api/products", request)
                .build()
                .convertBody(ProductResponse.class);
    }

    protected List<ProductResponse> 상품_리스트_조회() {
        return request()
                .get("/api/products")
                .build()
                .convertBodyToList(ProductResponse.class);
    }

    protected MenuResponse 메뉴_등록(MenuRequest request) {
        return request()
                .post("/api/menus", request)
                .build()
                .convertBody(MenuResponse.class);
    }

    protected List<MenuResponse> 메뉴_리스트_조회() {
        return request()
                .get("/api/menus")
                .build()
                .convertBodyToList(MenuResponse.class);
    }

    protected OrderResponse 주문_등록(OrderCreatedRequest request) {
        return request()
                .post("/api/orders", request)
                .build()
                .convertBody(OrderResponse.class);
    }

    protected List<OrderResponse> 주문_리스트_조회() {
        return request()
                .get("/api/orders")
                .build()
                .convertBodyToList(OrderResponse.class);
    }

    protected OrderResponse 주문_상태_변경(Long orderId, OrderChangeStatusRequest request) {
        return request()
                .put("/api/orders/" + orderId + "/order-status", request)
                .build()
                .convertBody(OrderResponse.class);
    }

    protected OrderTableResponse 주문_테이블_등록(OrderTableCreateRequest request) {
        return request()
                .post("/api/tables", request)
                .build()
                .convertBody(OrderTableResponse.class);
    }

    protected List<OrderTableResponse> 주문_테이블_리스트_조회() {
        return request()
                .get("/api/tables")
                .build()
                .convertBodyToList(OrderTableResponse.class);
    }

    protected OrderTableResponse 주문_테이블_착석(Long orderTableId, OrderTableChangeEmptyRequest request) {
        return request()
                .put("/api/tables/" + orderTableId + "/empty", request)
                .build()
                .convertBody(OrderTableResponse.class);
    }

    protected OrderTableResponse 주문_테이블_인원_변경(Long orderTableId, OrderTableChangeGuestRequest request) {
        return request()
                .put("/api/tables/" + orderTableId + "/number-of-guests", request)
                .build()
                .convertBody(OrderTableResponse.class);
    }

    protected TableGroupResponse 테이블_그룹_등록(TableGroupCreateRequest request) {
        return request()
                .post("/api/table-groups", request)
                .build()
                .convertBody(TableGroupResponse.class);
    }

    protected HttpResponse 테이블_그룹_해제(Long tableGroupId) {
        return request()
                .delete("/api/table-groups/" + tableGroupId)
                .build();
    }
}


