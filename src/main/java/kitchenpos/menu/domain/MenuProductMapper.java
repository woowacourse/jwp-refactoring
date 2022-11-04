package kitchenpos.menu.domain;

import kitchenpos.common.Price;
import org.springframework.stereotype.Component;

@Component
public class MenuProductMapper {
    public MenuProduct mapMenuProduct(MenuProduct save, Price price) {
        return new MenuProduct(save.getSeq(),
                save.getMenuId(),
                save.getProductId(),
                save.getQuantity(),
                price);
    }
}
