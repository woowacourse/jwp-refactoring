package kitchenpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.reqeust.MenuGroupCreateRequest;
import kitchenpos.ui.dto.request.ProductCreateRequest;

public class KitchenPosFixtures {
    public static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static final String 프로덕트_URL = "/api/products";
    public static final String 테이블_URL = "/api/tables";
    public static final String 테이블그룹_URL = "/api/table-groups";
    public static final String 메뉴그룹_URL = "/api/menu-groups";
    public static final String 메뉴_URL = "/api/menus";
    public static final String 주문_URL = "/api/orders";

    public static final OrderTable 삼인용_테이블 = new OrderTable(null, 3, true);
    public static final OrderTable 오인용_테이블 = new OrderTable(null, 5, true);
    public static final ProductCreateRequest 까르보치킨_생성요청 = new ProductCreateRequest("까르보치킨", new BigDecimal(20000));
    public static final ProductCreateRequest 짜장치킨_생성요청 = new ProductCreateRequest("짜장치킨", new BigDecimal(19000));
    public static final MenuGroupCreateRequest 두_마리_메뉴_생성요청 = new MenuGroupCreateRequest("두 마리 메뉴");
    public static final MenuGroupCreateRequest 세_마리_메뉴_생성요청 = new MenuGroupCreateRequest("세 마리 메뉴");
    public static final MenuGroupCreateRequest 네_마리_메뉴_생성요청 = new MenuGroupCreateRequest("네 마리 메뉴");
}
