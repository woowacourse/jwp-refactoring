package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        final BigDecimal price = menuDto.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menuDto.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProductDto> menuProductDtos = menuDto.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            final ProductDto productDto = productDao.findById(menuProductDto.getProductId())
                                                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(productDto.getPrice().multiply(BigDecimal.valueOf(menuProductDto.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final MenuDto savedMenuDto = menuDao.save(menuDto);

        final Long menuId = savedMenuDto.getId();
        final List<MenuProductDto> savedMenuProductDtos = new ArrayList<>();
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            menuProductDto.setMenuId(menuId);
            savedMenuProductDtos.add(menuProductDao.save(menuProductDto));
        }
        savedMenuDto.setMenuProducts(savedMenuProductDtos);

        return savedMenuDto;
    }

    public List<MenuDto> list() {
        final List<MenuDto> menuDtos = menuDao.findAll();

        for (final MenuDto menuDto : menuDtos) {
            menuDto.setMenuProducts(menuProductDao.findAllByMenuId(menuDto.getId()));
        }

        return menuDtos;
    }
}
