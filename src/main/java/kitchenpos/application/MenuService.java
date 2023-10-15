package kitchenpos.application;

import kitchenpos.application.dto.MenuRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.application.dto.MenuRequest.MenuProductRequest;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductDao menuProductDao,
            final ProductRepository productRepository
    ) {
        this.menuDao = menuDao;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProductRequest> menuProducts = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(request.toMenu());

        final Long menuId = savedMenu.getId();
        List<MenuProduct> savedMenuProducts = menuProducts.stream()
                .map(it -> menuProductDao.save(new MenuProduct(menuId, it.getProductId(), it.getQuantity())))
                .collect(Collectors.toList());
        savedMenu.changeMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }
        return menus;
    }
}
