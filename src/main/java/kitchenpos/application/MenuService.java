package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public Menu create(final MenuCreateRequest menuCreateRequest) {
        final BigDecimal price = new BigDecimal(menuCreateRequest.getPrice());

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProductRequest> menuProductRequests = menuCreateRequest.getMenuProductRequests();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu menu = new Menu(menuCreateRequest.getName(), price, menuCreateRequest.getMenuGroupId());
        final Menu savedMenu = menuDao.save(menu);

        menuProductRequests.stream()
            .map(request -> new MenuProduct(savedMenu.getId(), request.getProductId(), request.getQuantity()))
            .forEach(menuProduct -> menuProductDao.save(menuProduct));

        return savedMenu;
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
