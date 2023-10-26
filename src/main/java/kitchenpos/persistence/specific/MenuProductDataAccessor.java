package kitchenpos.persistence.specific;

import java.util.List;
import kitchenpos.persistence.BasicDataAccessor;
import kitchenpos.persistence.dto.MenuProductDataDto;

public interface MenuProductDataAccessor extends BasicDataAccessor<MenuProductDataDto> {

    List<MenuProductDataDto> findAllByMenuId(Long menuId);
}
