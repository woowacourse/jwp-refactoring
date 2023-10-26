package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.repository.converter.MenuProductConverter;
import kitchenpos.persistence.dto.MenuProductDataDto;
import kitchenpos.persistence.specific.MenuProductDataAccessor;
import org.springframework.stereotype.Repository;

@Repository
public class MenuProductRepository extends
        BaseRepository<MenuProduct, MenuProductDataDto, MenuProductDataAccessor, MenuProductConverter> {

    public MenuProductRepository(final MenuProductDataAccessor dataAccessor,
                                 final MenuProductConverter converter) {
        super(dataAccessor, converter);
    }

    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return converter.dataToEntity(dataAccessor.findAllByMenuId(menuId));
    }
}
