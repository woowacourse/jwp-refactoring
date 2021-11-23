package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.menu.NoSuchMenuGroupException;
import kitchenpos.exception.product.NoSuchProductException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(toList());
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(NoSuchMenuGroupException::new);

        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();

        List<Long> menuProductsId = menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());

        List<Product> foundProducts = findAllByIds(menuProductsId);

        List<MenuProduct> menuProducts = mapToMenuProducts(menuProductRequests, foundProducts);

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);

        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<Product> findAllByIds(List<Long> menuProductsId) {
        List<Product> products = productRepository.findAllById(menuProductsId);

        validateMenuProductsIds(menuProductsId, products);

        return products;
    }

    private void validateMenuProductsIds(List<Long> menuProductsId, List<Product> products) {
        if (menuProductsId.size() != products.size()) {
            throw new NoSuchProductException();
        }
    }

    private List<MenuProduct> mapToMenuProducts(List<MenuProductRequest> menuProductRequests, List<Product> foundProducts) {
        return foundProducts.stream()
                .map(foundProduct -> mapToMenuProduct(menuProductRequests, foundProduct))
                .collect(toList());
    }

    private MenuProduct mapToMenuProduct(List<MenuProductRequest> menuProductRequests, Product foundProduct) {
        return menuProductRequests.stream()
                .filter(menuProductRequest -> foundProduct.getId().equals(menuProductRequest.getProductId()))
                .findAny()
                .map(menuProductRequest -> new MenuProduct(foundProduct, menuProductRequest.getQuantity()))
                .orElseThrow(NoSuchProductException::new);
    }
}
