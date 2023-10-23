package kitchenpos.application.menu;

import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuMapper {

    public Menu toMenu(MenuRequest menuRequest, MenuValidator menuValidator) {
        List<MenuProduct> menuProducts = menuRequest.getMenuProducts().stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
        return Menu.createWithoutId(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuProducts, menuValidator);
    }

}
