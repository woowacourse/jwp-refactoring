package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.MenuRequestAssembler;
import kitchenpos.menu.application.dto.request.menu.MenuRequest;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.application.dto.response.MenuResponseAssembler;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuRequestAssembler requestAssembler;
    private final MenuResponseAssembler responseAssembler;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository,
                       final MenuRequestAssembler requestAssembler,
                       final MenuResponseAssembler responseAssembler
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.requestAssembler = requestAssembler;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validatePossibleToCreate(request);

        final var menu = requestAssembler.asMenu(request);
        final var savedMenu = menuRepository.save(menu);
        return responseAssembler.asMenuResponse(savedMenu);
    }

    private void validatePossibleToCreate(final MenuRequest request) {
        validateMenuGroupExist(request.getMenuGroupId());
        validateMenuPriceIsNotLowerThanTotalPrice(request);
    }

    private void validateMenuGroupExist(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹을 찾을 수 없습니다.");
        }
    }

    private void validateMenuPriceIsNotLowerThanTotalPrice(final MenuRequest request) {
        final var menuPrice = request.getPrice();
        final var totalPrice = calculateTotalPrice(request);

        if (menuPrice.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품 금액 합산보다 클 수 없습니다.");
        }
    }

    private Price calculateTotalPrice(final MenuRequest request) {
        final var menuProducts = requestAssembler.asMenuProducts(request.getMenuProducts());

        BigDecimal sum = BigDecimal.ZERO;
        for (final var menuProduct : menuProducts) {
            final var productPrice = asProduct(menuProduct.getProductId()).getPrice();
            final var quantity = menuProduct.getQuantity();

            final var result = productPrice.multiply(quantity.getValue());
            sum = sum.add(result.getValue());
        }
        return new Price(sum);
    }

    private Product asProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    public List<MenuResponse> list() {
        final var menus = menuRepository.findAll();
        return responseAssembler.asMenuResponses(menus);
    }
}
