package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        final List<MenuProductDto> menuProductDtos = menuDto.getMenuProductDtos();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            Product product = productDao.findById(menuProductDto.getProductId())
                                        .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(
                product.getPrice().multiply(BigDecimal.valueOf(menuProductDto.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu savedMenu = menuDao.save(toEntity(menuDto));
        MenuDto savedMenuDto = MenuDto.from(savedMenu);
        final List<MenuProductDto> savedMenuProductDtos = new ArrayList<>();
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            menuProductDto.setMenuId(savedMenu.getId());
            MenuProduct savedMenuProduct = menuProductDao.save(toEntity(menuProductDto, savedMenu));
            savedMenuProductDtos.add(menuProductDto);
        }
        savedMenuDto.setMenuProductDtos(savedMenuProductDtos);

        return savedMenuDto;
    }

    private MenuProduct toEntity(MenuProductDto menuProductDto, Menu menu) {
        Product product = productDao.findById(menuProductDto.getProductId())
                                    .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(menuProductDto.getSeq(), menu, product, menuProductDto.getQuantity());
    }

    private Menu toEntity(MenuDto menuDto) {
        MenuGroup menuGroup = menuGroupDao.findById(menuDto.getMenuGroupId())
                                          .orElseThrow(IllegalArgumentException::new);
        return new Menu(
            menuDto.getId(),
            menuDto.getName(),
            menuDto.getPrice(),
            menuGroup,
            null
        );
    }

    public List<MenuDto> list() {
        final List<MenuDto> menuDtos = menuDao.findAll()
                                              .stream()
                                              .map(MenuDto::from)
                                              .collect(toList());

        for (final MenuDto menuDto : menuDtos) {
            List<MenuProductDto> menuProductDtos = menuProductDao.findAllByMenuId(menuDto.getId())
                                                                 .stream()
                                                                 .map(MenuProductDto::from)
                                                                 .collect(toList());
            menuDto.setMenuProductDtos(menuProductDtos);
        }

        return menuDtos;
    }
}
