package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.common.vo.Money;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.menu.CreateMenuRequest;
import kitchenpos.menu.dto.menu.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final CreateMenuRequest createMenuRequest) {
        validate(createMenuRequest);
        final MenuGroup menuGroup = menuGroupRepository.findById(createMenuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final Menu menu = menuRepository.save(createMenuRequest.toDomainWith(menuGroup));
        final List<MenuProduct> menuProducts = createMenuRequest.getMenuProducts().stream()
                .map(menuProductRequest -> new MenuProduct(menu, menuProductRequest.getProductId(), menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
        menuProductRepository.saveAll(menuProducts);
        return menu;
    }

    private void validate(final CreateMenuRequest createMenuRequest) {
        validatePrice(createMenuRequest);
        validateProductExists(createMenuRequest);
    }

    private void validatePrice(final CreateMenuRequest createMenuRequest) {
        final Money price = Money.valueOf(createMenuRequest.getPrice());
        if (price.isBiggerThan(calculateMoney(createMenuRequest.getMenuProducts()))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateProductExists(final CreateMenuRequest createMenuRequest) {
        createMenuRequest.getMenuProducts()
                .forEach(menuProductRequest -> productRepository.findById(menuProductRequest.getProductId()).orElseThrow(IllegalArgumentException::new));
    }

    private Money calculateMoney(final List<MenuProductRequest> menuProductRequests) {
        Money sum = Money.empty();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(Money.valueOf(menuProductRequest.getQuantity())));
        }
        return sum;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
