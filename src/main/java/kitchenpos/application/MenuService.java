package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupDao menuGroupDao, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Menu menu = request.toMenu();
        List<MenuProduct> menuProducts = request.toMenuProducts();
        validate(menu.getMenuGroupId(), menu.getPrice(), menuProducts);
        menuRepository.save(menu);
        menu.addMenuProducts(menuProducts);
        return MenuResponse.from(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private void validate(Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다. menuGroupId = " + menuGroupId);
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다. productId = " + menuProduct.getProductId()));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException(
                    String.format("메뉴의 가격은 상품 전체 가격의 합보다 클 수 없습니다. price = %f, product price sum = %f", price, sum));
        }
    }
}
