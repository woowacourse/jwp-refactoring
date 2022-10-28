package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.CreateMenuProductsRequest;
import kitchenpos.ui.dto.CreateMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private static final String NOT_FOUND_PRODUCT_ERROR_MESSAGE = "존재하지 않는 상품 ID 입니다.";

    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final CreateMenuRequest request) {
        validateMenuGroup(request.getMenuGroupId());

        BigDecimal price = request.getPrice();
        List<CreateMenuProductsRequest> menuProductsRequests = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final CreateMenuProductsRequest menuProduct : menuProductsRequests) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_ERROR_MESSAGE));
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
            validateProduct(productId);

            MenuProduct menuProduct = new MenuProduct(menuId, productId, quantity);
            menuProductDao.save(menuProduct);
        }

        return savedMenu;
    }

    private void validateProduct(final Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException(NOT_FOUND_PRODUCT_ERROR_MESSAGE);
        }
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new NotFoundException("존재하지 않는 메뉴그룹 ID 입니다.");
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
