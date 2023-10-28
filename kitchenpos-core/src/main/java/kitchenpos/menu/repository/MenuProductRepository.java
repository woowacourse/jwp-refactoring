package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.persistence.MenuProductDataAccessor;
import kitchenpos.menu.persistence.dto.MenuProductDataDto;
import kitchenpos.menu.repository.converter.MenuProductConverter;
import kitchenpos.support.BaseRepository;
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
