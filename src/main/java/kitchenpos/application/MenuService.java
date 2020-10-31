package kitchenpos.application;

import kitchenpos.dto.MenuDetailResponse;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.ProductQuantityRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    public Menu create(final MenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<ProductQuantityRequest> productQuantities = menuRequest.getProductQuantities();

        BigDecimal sum = BigDecimal.ZERO;
        for (final ProductQuantityRequest productQuantityRequest : productQuantities) {
            final Product product = productRepository.findById(productQuantityRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(productQuantityRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menuRequest.toEntity());

        final Long menuId = savedMenu.getId();

        for (final ProductQuantityRequest productQuantityRequest : productQuantities) {
            menuProductRepository.save(new MenuProduct(menuId, productQuantityRequest.getProductId(), productQuantityRequest
                    .getQuantity()));
        }

        return savedMenu;
    }

    public List<MenuDetailResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        final List<MenuDetailResponse> menuProductResponses = new ArrayList<>();

        for (final Menu menu : menus) {
            List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());
            menuProductResponses.add(
                    new MenuDetailResponse(
                            menu.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            MenuProductResponse.ofList(menuProducts)
                    )
            );
        }

        return menuProductResponses;
    }
}
