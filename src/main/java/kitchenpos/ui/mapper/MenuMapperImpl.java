package kitchenpos.ui.mapper;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuMapperImpl implements MenuMapper {

    @Override
    public Menu toMenu(final MenuCreateRequest menuCreateRequest, final List<MenuProduct> menuProducts) {
        if (menuCreateRequest == null) {
            return null;
        }

        return new Menu(
                null,
                menuCreateRequest.getName(),
                new Price(menuCreateRequest.getPrice()),
                menuCreateRequest.getMenuGroupId(),
                menuProducts
        );
    }
}
