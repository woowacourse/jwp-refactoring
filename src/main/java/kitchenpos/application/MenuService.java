package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        validateMenuPrice(request.getPrice(), request.getMenuProducts());

        return MenuResponse.from(menuRepository.save(request.toEntity()));
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
