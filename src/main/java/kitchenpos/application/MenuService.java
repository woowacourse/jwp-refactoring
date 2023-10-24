package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.CreateMenuProductRequest;
import kitchenpos.ui.dto.CreateMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Menu create(final CreateMenuRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        final MenuGroup savedMenuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                            .orElseThrow(() -> new IllegalArgumentException());

        final List<CreateMenuProductRequest> createMenuProductRequests = request.getMenuProducts();
        final List<MenuProduct> menuProducts = new ArrayList<>();

        BigDecimal sum = BigDecimal.ZERO;
        for (final CreateMenuProductRequest createMenuProductRequest : createMenuProductRequests) {
            final Product product = productRepository.findById(createMenuProductRequest.getProductId())
                                                     .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(createMenuProductRequest.getQuantity())));

            menuProducts.add(createMenuProductRequest.toEntity(product));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu menu = request.toEntity(savedMenuGroup, menuProducts);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(menu);
            savedMenuProducts.add(menuProduct);
        }
        menu.addMenuProducts(savedMenuProducts);

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
