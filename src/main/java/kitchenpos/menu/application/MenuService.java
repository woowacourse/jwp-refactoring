package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductDto;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductQuantities;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
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
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        validateExistMenuGroup(menuCreateRequest.getMenuGroupId());
        Price price = Price.ofMenu(
                menuCreateRequest.getPrice(),
                toProductQuantities(menuCreateRequest.getMenuProducts())
        );
        return MenuResponse.from(saveMenu(menuCreateRequest, price));
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductDto> menuProducts) {
            return menuProducts.stream()
                    .map(MenuProductDto::toMenuProduct)
                    .collect(Collectors.toList());
        }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAllWithMenuProducts();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private void validateExistMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private Menu saveMenu(final MenuCreateRequest menuCreateRequest, final Price price) {
        return menuRepository.save(new Menu(
                menuCreateRequest.getName(),
                price,
                menuCreateRequest.getMenuGroupId(),
                toMenuProducts(menuCreateRequest.getMenuProducts())
        ));
    }

    private ProductQuantities toProductQuantities(final List<MenuProductDto> menuProductsDto) {
        return new ProductQuantities(
                menuProductsDto.stream()
                        .map(MenuProductDto::toMenuProduct)
                        .collect(Collectors.toMap(
                                menuProduct -> getProductById(menuProduct.getProductId()),
                                menuProduct -> new Quantity(menuProduct.getQuantity())
                        )));
    }

    private Product getProductById(final Long productId) {
        return productRepository.findById(productId).orElseThrow(IllegalArgumentException::new);
    }
}
