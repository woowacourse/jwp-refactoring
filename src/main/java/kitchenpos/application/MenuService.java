package kitchenpos.application;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
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
    public MenuResponse create(final MenuRequest request) {
        Menu menu = request.toEntityWithoutMenuProducts();
        final List<MenuProductRequest> menuProducts = request.getMenuProducts();

        if (menu.isPriceBiggerThen(sumProductPrices(menuProducts)) ||
            !menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu savedMenu = menuDao.save(menu);
        return menuProducts.stream()
            .map(menuProductRequest -> menuProductDao.save(menuProductRequest.toEntity(savedMenu.getId())))
            .collect(
                collectingAndThen(toList(), products -> MenuResponse.of(savedMenu, MenuProductResponse.of(products))));
    }

    public List<MenuResponse> list() {
        return menuDao.findAll()
            .stream()
            .map(menu -> MenuResponse.of(menu, MenuProductResponse.of(menuProductDao.findAllByMenuId(menu.getId()))))
            .collect(toList());
    }

    private Price sumProductPrices(List<MenuProductRequest> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return new Price(sum);
    }
}
