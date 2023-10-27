package kitchenpos.menu.ui;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.application.dto.request.MenuProductRequest;
import kitchenpos.menu.application.dto.request.ProductCreateRequest;

public class MenuRequestFixture {

    // 메뉴 그룹
    public static MenuGroupCreateRequest menuGroupCreateRequest() {
        return new MenuGroupCreateRequest("추천메뉴");
    }

    // 메뉴
    public static MenuCreateRequest menuCreateRequest() {
        return new MenuCreateRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1,
                List.of(new MenuProductRequest(1, 2))
        );
    }

    // 상품
    public static ProductCreateRequest productCreateRequest() {
        return new ProductCreateRequest("강정치킨", BigDecimal.valueOf(17000));
    }
}
