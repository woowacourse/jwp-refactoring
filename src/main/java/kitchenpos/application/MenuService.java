package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.Quantity;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
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
        List<MenuProduct> menuProducts = toMenuProducts(menuCreateRequest.getMenuProducts());
        Menu menu = new Menu(menuCreateRequest.getName(), menuPrice, findMenuGroup(menuCreateRequest), menuProducts);
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

    private MenuGroup findMenuGroup(MenuCreateRequest menuCreateRequest) {
        return menuGroupRepository.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
