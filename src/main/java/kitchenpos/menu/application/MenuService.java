package kitchenpos.menu.application;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuName;
import kitchenpos.menu.MenuPrice;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menugroup.application.MenuGroupRepository;
import kitchenpos.menuproduct.MenuProduct;
import kitchenpos.menuproduct.MenuQuantity;
import kitchenpos.menuproduct.application.request.MenuProductRequest;
import kitchenpos.product.Product;
import kitchenpos.product.application.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }


    public Menu create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        final List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(m -> new MenuProduct(
                        m.getProductId(),
                        new MenuQuantity(m.getQuantity())
                ))
                .collect(Collectors.toList());

        return saveMenu(request, request.getPrice(), menuProducts);
    }

    private Menu saveMenu(final MenuRequest request, final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = getSumOfMenuProductRequests(menuProducts);
        validatePriceAndSum(price, sum);
        return menuRepository.save(
                new Menu(
                        new MenuName(request.getName()),
                        new MenuPrice(request.getPrice()),
                        request.getMenuGroupId(),
                        menuProducts
                )
        );
    }

    private BigDecimal getSumOfMenuProductRequests(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantityValue())));
        }
        return sum;
    }

    private void validatePriceAndSum(final BigDecimal price, final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
