package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class MenuFixture {

    private final MenuRepository menuRepository;

    public MenuFixture(MenuRepository menuRepository) {
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
}
