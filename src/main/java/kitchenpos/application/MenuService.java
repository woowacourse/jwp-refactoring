package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        final BigDecimal price = request.getPrice();

        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 메뉴 그룹이 존재하지 않습니다."));

        final List<MenuProduct> menuProducts = getMenuProducts(request.getMenuProducts(), price);
        final Menu menu = new Menu(menuGroup, menuProducts, request.getName(), price);
        return menuRepository.save(menu);
    }

    private List<MenuProduct> getMenuProducts(
            final List<MenuProductRequest> menuProductRequests,
            final BigDecimal price
    ) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        BigDecimal amountSum = BigDecimal.ZERO;

        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 존재하지 않습니다."));

            final MenuProduct menuProduct = new MenuProduct(null, product, menuProductRequest.getQuantity());
            amountSum = menuProduct.calculateAmount();
            menuProducts.add(menuProduct);
        }

        validateMenuPrice(price, amountSum);
        return menuProducts;
    }

    private void validateMenuPrice(final BigDecimal price, final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 금액(가격 * 수량)의 합보다 클 수 없습니다.");
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
