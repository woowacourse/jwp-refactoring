package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.application.dto.response.MenuQueryResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.repository.ProductRepository;
import kitchenpos.menu.persistence.MenuGroupRepositoryImpl;
import kitchenpos.menu.persistence.MenuRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepositoryImpl menuRepository,
            final MenuGroupRepositoryImpl menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }
 
    @Transactional
    public MenuQueryResponse create(final MenuCreateRequest request) {
        validateMenuRequest(request, new Price(request.getPrice()));
        final Menu menu = request.toMenu();

        final Menu savedMenu = menuRepository.save(menu);

        return MenuQueryResponse.from(savedMenu);
    }

    private void validateMenuRequest(
            final MenuCreateRequest request, final Price price) {
        final List<MenuProductCreateRequest> menuProducts = request.getMenuProducts();
        final Price totalPrice = calculateTotalPrice(menuProducts);
        validateMenuPrice(price, totalPrice);
        validateExistMenuGroup(request.getMenuGroupId());
    }

    private Price calculateTotalPrice(final List<MenuProductCreateRequest> menuProducts) {
        Price totalPrice = Price.ZERO;
        for (final MenuProductCreateRequest menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            totalPrice = totalPrice.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }
        return totalPrice;
    }

    private void validateMenuPrice(final Price price, final Price totalPrice) {
        if (price.isGreaterThan(totalPrice)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuQueryResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuQueryResponse::from)
                .collect(Collectors.toList());
    }
}
