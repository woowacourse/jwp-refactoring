package core.application.menu;

import core.application.dto.MenuRequest;
import core.domain.Menu;
import core.domain.MenuProduct;
import core.domain.MenuValidator;
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
