package kitchenpos.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.resitory.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        Price menuPrice = new Price(menuCreateRequest.getPrice());
        validateMenuGroup(menuCreateRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = toMenuProducts(menuCreateRequest.getMenuProducts());
        Menu menu = new Menu(menuCreateRequest.getName(), menuPrice, menuCreateRequest.getMenuGroupId(), menuProducts);
        return new MenuResponse(menuRepository.save(menu));
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductCreateRequest> menuProductCreateRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductCreateRequest menuProductResponse : menuProductCreateRequests) {
            Product product = productRepository.findById(menuProductResponse.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            MenuProduct menuProduct = new MenuProduct(product, new Quantity(menuProductResponse.getQuantity()));
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuGroupNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
