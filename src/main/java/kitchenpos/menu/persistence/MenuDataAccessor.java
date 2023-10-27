package kitchenpos.menu.persistence;

import java.util.List;
import kitchenpos.menu.persistence.dto.MenuDataDto;
import kitchenpos.support.BasicDataAccessor;

public interface MenuDataAccessor extends BasicDataAccessor<MenuDataDto> {

    long countByIdIn(List<Long> ids);
}
