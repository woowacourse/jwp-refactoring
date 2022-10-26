package kitchenpos.ui.mapper;

import java.util.ArrayList;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuMapperImpl implements MenuMapper {

    @Override
    public Menu menuCreateRequestToMenu(final MenuCreateRequest menuCreateRequest) {
        if (menuCreateRequest == null) {
            return null;
        }

        Menu menu = new Menu(
                null,
                menuCreateRequest.getName(),
                new Price(menuCreateRequest.getPrice()),
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
}
