package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductDto;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        validateExistMenuGroup(menuCreateRequest.getMenuGroupId());
        BigDecimal sum = calculateSum(extractMenuProductsFrom(menuCreateRequest.getMenuProductsDto()));
        validateSum(menuCreateRequest.getPrice(), sum);
        return saveMenu(menuCreateRequest);
    }

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

    private MenuResponse saveMenu(final MenuCreateRequest menuCreateRequest) {
        final Menu savedMenu = menuRepository.save(menuCreateRequest.toMenu());
        return MenuResponse.from(savedMenu);
    }

    private void validateSum(final BigDecimal price, final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateSum(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    private List<MenuProduct> extractMenuProductsFrom(final List<MenuProductDto> menuProductsDto) {
        return menuProductsDto.stream()
                .map(MenuProductDto::toMenuProduct)
                .collect(Collectors.toList());
    }
}
