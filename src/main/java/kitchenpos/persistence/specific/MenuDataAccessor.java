package kitchenpos.persistence.specific;

import java.util.List;
import kitchenpos.persistence.BasicDataAccessor;
import kitchenpos.persistence.dto.MenuDataDto;

public interface MenuDataAccessor extends BasicDataAccessor<MenuDataDto> {

    long countByIdIn(List<Long> ids);
}
