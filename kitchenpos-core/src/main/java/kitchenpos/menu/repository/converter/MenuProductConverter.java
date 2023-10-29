package kitchenpos.menu.repository.converter;

import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.persistence.dto.MenuProductDataDto;
import kitchenpos.support.Converter;
import org.springframework.stereotype.Component;

@Component
public class MenuProductConverter implements Converter<MenuProduct, MenuProductDataDto> {

    @Override
    public MenuProductDataDto entityToData(final MenuProduct menuProduct) {
        return new MenuProductDataDto(
                menuProduct.getSeq(),
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }

    @Override
    public MenuProduct dataToEntity(final MenuProductDataDto menuProductDataDto) {
        return new MenuProduct(
                menuProductDataDto.getSeq(),
                menuProductDataDto.getMenuId(),
                menuProductDataDto.getProductId(),
                menuProductDataDto.getQuantity()
        );
    }
}
