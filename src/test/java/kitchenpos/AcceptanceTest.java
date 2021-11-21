package kitchenpos;

import kitchenpos.testtool.RequestBuilder;
import kitchenpos.testtool.request.RequestApi;
import kitchenpos.ui.dto.request.MenuGroupRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AcceptanceTest {

    @Autowired
    private RequestBuilder request;

    protected RequestApi request() {
        return request.builder();
    }

    public MenuGroupResponse 메뉴그룹_등록(MenuGroupRequest request){
        return request()
                .post("/api/menu-groups", request)
                .build()
                .convertBody(MenuGroupResponse.class);
    }
}
