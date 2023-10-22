package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuName;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public Menu create(final MenuRequest request) {
        final BigDecimal price = request.getPrice();
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        final Menu savedMenu = saveMenu(request, price, menuProductRequests);
        saveMenuProducts(menuProductRequests, savedMenu);

        return savedMenu;
    }

    private Menu saveMenu(final MenuRequest request, final BigDecimal price, final List<MenuProductRequest> requests) {
        final BigDecimal sum = getSumOfMenuProductRequests(requests);
        validatePriceAndSum(price, sum);
        return menuRepository.save(
                new Menu(new MenuName(request.getName()), new MenuPrice(request.getPrice()), request.getMenuGroupId())
        );
    }

    private BigDecimal getSumOfMenuProductRequests(final List<MenuProductRequest> menuProductRequests) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }
        return sum;
    }

    private void validatePriceAndSum(final BigDecimal price, final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private void saveMenuProducts(final List<MenuProductRequest> requests, final Menu menu) {
        for (final MenuProductRequest request : requests) {
            menuProductRepository.save(
                    new MenuProduct(menu.getId(), request.getProductId(), new Quantity(request.getQuantity()))
            );
        }
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
