package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.dto.CreateMenuProductsRequest;
import kitchenpos.ui.dto.CreateMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final CreateMenuRequest request) {
        BigDecimal price = request.getPrice();

        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<CreateMenuProductsRequest> menuProductsRequests = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final CreateMenuProductsRequest menuProduct : menuProductsRequests) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(
                new Menu(request.getName(), request.getPrice(), request.getMenuGroupId()));
        final Long menuId = savedMenu.getId();

        for (CreateMenuProductsRequest menuProductsRequest : menuProductsRequests) {
            Long productId = menuProductsRequest.getProductId();
            long quantity = menuProductsRequest.getQuantity();
            Product product = productDao.findById(productId)
                    .orElseThrow(NoSuchElementException::new);

            MenuProduct menuProduct = new MenuProduct(menuId, productId, quantity);
            menuProductDao.save(menuProduct);
        }

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
