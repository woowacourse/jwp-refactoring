package kitchenpos.domain.service;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuCreateService {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuCreateService(final ProductRepository productRepository,
                             final MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
        final List<MenuProduct> menuProducts = createMenuProducts(request);
        validateSumOfPrice(request, menuProducts);

        return new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                menuProducts);
    }

    private List<MenuProduct> createMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts().stream()
                .map(menuProductRequest -> new MenuProduct(
                        menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateSumOfPrice(final MenuCreateRequest request, final List<MenuProduct> menuProducts) {
        final Price sum = menuProducts.stream()
                .map(this::findProductOrThrow)
                .reduce(Price.ZERO, Price::add);

        if (request.getPrice().isBiggerThan(sum)) {
            throw new IllegalArgumentException("메뉴 가격은 상품 가격들의 합보다 클 수 없습니다.");
        }
    }

    private Price findProductOrThrow(final MenuProduct menuProduct) {
        final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return product.calculatePrice(menuProduct.getQuantity());
    }
}
