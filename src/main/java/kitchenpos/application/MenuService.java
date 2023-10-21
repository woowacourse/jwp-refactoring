package kitchenpos.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.MenuException.NotExistsProductException;
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

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.getById(menuRequest.getMenuGroupId());
        List<Product> products = productRepository.findAllById(menuRequest.getProductIds());
        validate(menuRequest, products);
        List<MenuProduct> menuProducts = getMenuProducts(products, menuRequest.getMenuProductRequests());

        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
        return menuRepository.save(menu);
    }

    private void validate(final MenuRequest menuRequest, final List<Product> products) {
        if (products.size() != menuRequest.getProductSize()) {
            throw new NotExistsProductException();
        }
    }

    private List<MenuProduct> getMenuProducts(final List<Product> products,
                                              final List<MenuProductRequest> menuProductRequests) {
        Map<Long, Product> productById = new HashMap<>();
        for (Product product : products) {
            productById.put(product.getId(), product);
        }

        return menuProductRequests.stream()
                .map(menuProductRequest -> new MenuProduct(productById.get(menuProductRequest.getProductId()),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
