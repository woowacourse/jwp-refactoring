package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        validateMenuGroupExistence(request);
        validateMenuPrice(request.getPrice(), request.getMenuProducts());

        return MenuResponse.from(menuRepository.save(request.toEntity()));
    }

    private void validateMenuGroupExistence(MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuPrice(BigDecimal menuPrice, List<MenuProductCreateRequest> menuProductsRequest) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductCreateRequest menuProductRequest : menuProductsRequest) {
            Product product = productRepository.findById(menuProductRequest.getProductId());
            sum = sum.add(product.calculatePrice(menuProductRequest.getQuantity()));
        }
        validateMenuPriceLessThanTotalProductPrice(menuPrice, sum);
    }

    private void validateMenuPriceLessThanTotalProductPrice(BigDecimal menuPrice, BigDecimal productsSum) {
        if (menuPrice.compareTo(productsSum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return toMenuResponses(menus);
    }

    private List<MenuResponse> toMenuResponses(List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
