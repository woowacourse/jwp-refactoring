package kitchenpos.menu.persistence;

import java.util.List;
import kitchenpos.menu.persistence.dto.MenuProductDataDto;
import kitchenpos.support.BasicDataAccessor;

public interface MenuProductDataAccessor extends BasicDataAccessor<MenuProductDataDto> {

    List<MenuProductDataDto> findAllByMenuId(Long menuId);
}
