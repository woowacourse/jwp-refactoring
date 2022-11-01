package kitchenpos.dto.menu.mapper;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.request.MenuCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuMapperImpl implements MenuMapper {

    @Override
    public Menu toMenu(final MenuCreateRequest menuCreateRequest, final List<MenuProduct> menuProducts) {
        return new Menu(
                menuCreateRequest.getName(),
                menuCreateRequest.getPrice(),
                menuCreateRequest.getMenuGroupId(),
                menuProducts
        );
    }
}
