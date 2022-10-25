package kitchenpos.application.dto.response;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;

public record MenuResponse(Long id,
                           String name,
                           BigDecimal price,
                           Long menuGroupId,
                           List<MenuProductResponse> menuProductResponses) {

    public static MenuResponse from(final Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().getValues()
                .stream()
                .map(MenuProductResponse::from)
                .toList();
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                menuProductResponses);
    }
}
