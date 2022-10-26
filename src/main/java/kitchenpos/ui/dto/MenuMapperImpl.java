package kitchenpos.ui.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.ui.dto.response.MenuCreateResponse;
import kitchenpos.ui.dto.response.MenuProductCreateResponse;
import kitchenpos.ui.dto.response.MenuProductResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import org.springframework.stereotype.Component;

@Component
public class MenuMapperImpl implements MenuMapper {

    @Override
    public Menu createRequestToMenu(final MenuCreateRequest menuCreateRequest) {
        if (menuCreateRequest == null) {
            return null;
        }

        Menu menu = new Menu(
                null,
                menuCreateRequest.getName(),
                menuCreateRequest.getPrice(),
                menuCreateRequest.getMenuGroupId(),
                new ArrayList<>()
        );

        menuCreateRequest.getMenuProducts()
                .forEach(request -> createRequestToMenuProduct(request, menu));

        return menu;
    }

    private void createRequestToMenuProduct(final MenuProductCreateRequest menuProductCreateRequest, final Menu menu) {
        new MenuProduct(
                null,
                menu,
                menuProductCreateRequest.getProductId(),
                menuProductCreateRequest.getQuantity()
        );
    }

    @Override
    public MenuCreateResponse menuToCreateResponse(final Menu menu) {
        return new MenuCreateResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menu.getMenuProducts()
                        .stream()
                        .map(this::menuProductToCreateResponse)
                        .collect(Collectors.toList())
        );
    }

    private MenuProductCreateResponse menuProductToCreateResponse(final MenuProduct menuProduct) {
        return new MenuProductCreateResponse(
                menuProduct.getSeq(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }

    @Override
    public List<MenuResponse> menusToResponses(final List<Menu> menus) {
        return menus.stream()
                .map(this::menuToResponse)
                .collect(Collectors.toList());
    }

    private MenuResponse menuToResponse(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
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
}
