package kitchenpos.menu.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductDao;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuDao menuDao,
        final MenuGroupDao menuGroupDao,
        final MenuProductDao menuProductDao,
        final ProductRepository productRepository
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public Long create(final MenuCreateRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductDto menuProductDto : request.getMenuProductDtos()) {
            final Product product = productRepository.findById(menuProductDto.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            BigDecimal price = product.getProductPrice().getPrice();
            sum = sum.add(price.multiply(BigDecimal.valueOf(menuProductDto.getQuantity())));
        }

        BigDecimal price = BigDecimal.valueOf(request.getPrice());
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu menu = request.toEntity();

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProductDto menuProductDto : request.getMenuProductDtos()) {
            MenuProduct menuProduct = new MenuProduct(null, menuId, menuProductDto.getProductId(),
                menuProductDto.getQuantity());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.changeMenuProducts(savedMenuProducts);

        return savedMenu.getId();
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
