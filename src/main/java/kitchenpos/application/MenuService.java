package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.domain.product.Price;
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
    public MenuResponse create(final String name, final Price price, final Long menuGroupId, final List<MenuProductRequest> menuProductRequests) {
        validateMenuGroupIdExists(menuGroupId);

        final MenuProducts menuProducts = new MenuProducts(menuProductRequests
                .stream().map(this::getMenuProduct)
                .collect(Collectors.toList()));

        if (menuProducts.isBiggerThanTotalPrice(price)) {
            throw new IllegalArgumentException();
        }

        return MenuResponse.from(menuRepository.save(new Menu(name, price, menuGroupId, menuProducts)));
    }

    private void validateMenuGroupIdExists(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private MenuProduct getMenuProduct(final MenuProductRequest menuProductRequest) {
        final Product product = productRepository.findMandatoryById(menuProductRequest.getProductId());
        return new MenuProduct(product.getPrice(), product.getId(), menuProductRequest.getQuantity());
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }
}
