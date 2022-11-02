package kitchenpos.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuMapper(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public Menu from(final MenuRequest menuRequest) {
        return new Menu(
                menuRequest.getName(),
                new Price(menuRequest.getPrice()),
                getMenuGroup(menuRequest),
                getMenuProducts(menuRequest)
        );
    }

    private MenuGroup getMenuGroup(final MenuRequest request) {
        return menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> getMenuProducts(final MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(menuProductRequest -> new MenuProduct(
                        getProduct(menuProductRequest),
                        menuProductRequest.getQuantity()
                ))
                .collect(Collectors.toList());
        // TODO: 최적화 필요
    }

    private Product getProduct(final MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
