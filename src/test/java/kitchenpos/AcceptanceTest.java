package kitchenpos;

import kitchenpos.testtool.RequestBuilder;
import kitchenpos.testtool.request.RequestApi;
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
}


