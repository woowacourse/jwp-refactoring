package kitchenpos.support.fixture.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductSaveRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.application.dto.MenuSaveRequest;

public class MenuDtoFixture {

    public static MenuSaveRequest 메뉴_생성_요청(Menu menu, List<MenuProduct> menuProducts) {
        List<MenuProductSaveRequest> menuProductSaveRequests = menuProducts.stream()
            .map(it -> new MenuProductSaveRequest(it.getProductId(), it.getQuantity()))
            .collect(Collectors.toList());
        return new MenuSaveRequest(menu.getName().getValue(), menu.getPrice().getValue().intValue(),
            menu.getMenuGroupId(), menuProductSaveRequests);
    }

    public static MenuResponse 메뉴_생성_응답(Menu menu) {
        return MenuResponse.toResponse(menu);
    }
}
