package kitchenpos;

import kitchenpos.testtool.RequestBuilder;
import kitchenpos.testtool.request.RequestApi;
import kitchenpos.ui.dto.request.MenuGroupRequest;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.ui.dto.request.ProductRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import kitchenpos.ui.dto.response.ProductResponse;
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

    protected MenuGroupResponse 메뉴그룹_등록(MenuGroupRequest request){
        return request()
                .post("/api/menu-groups", request)
                .build()
                .convertBody(MenuGroupResponse.class);
    }

    protected List<MenuGroupResponse> 메뉴그룹_리스트_조회(){
        return request()
                .get("/api/menu-groups")
                .build()
                .convertBodyToList(MenuGroupResponse.class);
    }

    protected ProductResponse 상품_등록(ProductRequest request){
        return request()
                .post("/api/products", request)
                .build()
                .convertBody(ProductResponse.class);
    }

    protected List<ProductResponse> 상품_리스트_조회(){
        return request()
                .get("/api/products")
                .build()
                .convertBodyToList(ProductResponse.class);
    }

    protected MenuResponse 메뉴_등록(MenuRequest request){
        return request()
                .post("/api/menus", request)
                .build()
                .convertBody(MenuResponse.class);
    }

    protected List<MenuResponse> 메뉴_리스트_조회(){
        return request()
                .get("/api/menus")
                .build()
                .convertBodyToList(MenuResponse.class);
    }
}
