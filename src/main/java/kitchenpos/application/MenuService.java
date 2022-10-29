package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductDto;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductQuantities;
import kitchenpos.domain.Quantity;
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
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        validateExistMenuGroup(menuCreateRequest.getMenuGroupId());
        Price price = Price.ofMenu(
                menuCreateRequest.getPrice(),
                toProductQuantities(menuCreateRequest.getMenuProducts())
        );
        return saveMenu(menuCreateRequest, price);
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

    private MenuResponse saveMenu(final MenuCreateRequest menuCreateRequest, Price price) {
        final Menu savedMenu = menuRepository.save(menuCreateRequest.toMenu(price));
        return MenuResponse.from(savedMenu);
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
