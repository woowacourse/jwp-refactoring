package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.testtool.RequestBuilder;
import kitchenpos.menu.ui.dto.request.MenuProductRequest;
import kitchenpos.menu.ui.dto.request.MenuRequest;
import kitchenpos.menu.ui.dto.response.MenuResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class MenuFixture extends DefaultFixture {

    private final MenuRepository menuRepository;

    public MenuFixture(RequestBuilder requestBuilder, MenuRepository menuRepository) {
        super(requestBuilder);
        this.menuRepository = menuRepository;
    }

    public MenuRequest 메뉴_생성_요청(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductRequest> menuProducts
    ) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public Menu 메뉴_조회(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuResponse> 메뉴_응답_리스트_생성(MenuResponse... menuResponses) {
        return Arrays.asList(menuResponses);
    }

    public MenuResponse 메뉴_등록(MenuRequest request) {
        return request()
                .post("/api/menus", request)
                .build()
                .convertBody(MenuResponse.class);
    }

    public List<MenuResponse> 메뉴_리스트_조회() {
        return request()
                .get("/api/menus")
                .build()
                .convertBodyToList(MenuResponse.class);
    }
}
