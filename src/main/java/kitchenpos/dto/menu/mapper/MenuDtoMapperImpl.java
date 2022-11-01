package kitchenpos.dto.menu.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.response.MenuProductResponse;
import kitchenpos.dto.menu.response.MenuResponse;
import org.springframework.stereotype.Component;

@Component
public class MenuDtoMapperImpl implements MenuDtoMapper {

    @Override
    public MenuResponse toMenuResponse(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getValue(),
                menu.getMenuGroupId(),
                menu.getMenuProducts()
                        .stream()
                        .map(this::menuProductToResponse)
                        .collect(Collectors.toList())
        );
    }

    private MenuResponse menuToResponse(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getValue(),
                menu.getMenuGroupId(),
                menu.getMenuProducts()
                        .stream()
                        .map(this::menuProductToResponse)
                        .collect(Collectors.toList())
        );
    }

    private MenuProductResponse menuProductToResponse(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }

    @Override
    public List<MenuResponse> toMenuResponses(final List<Menu> menus) {
        return menus.stream()
                .map(this::menuToResponse)
                .collect(Collectors.toList());
    }
}
