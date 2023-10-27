package kitchenpos.application.menu;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuValidator;
import kitchenpos.dto.MenuRequest;

@Component
public class MenuMapper {

    public Menu toMenu(final MenuRequest menuRequest, final MenuValidator menuValidator) {
        final String name = menuRequest.getName();
        final BigDecimal price = menuRequest.getPrice();
        final Long menuGroupId = menuRequest.getMenuGroupId();
        final MenuProducts menuProducts = new MenuProducts(menuRequest.getMenuProductRequests().stream()
                .map(request -> new MenuProduct(request.getProductId(), request.getQuantity()))
                .collect(Collectors.toList()));
        return Menu.of(name, price, menuGroupId, menuProducts, menuValidator);
    }

}
