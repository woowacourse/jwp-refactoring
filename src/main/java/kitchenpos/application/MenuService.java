package kitchenpos.application;

import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.application.dto.response.CreateMenuResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.mapper.MenuMapper;
import kitchenpos.domain.mapper.MenuProductMapper;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuMapper menuMapper;
    private final MenuProductMapper menuProductMapper;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuMapper menuMapper,
            final MenuProductMapper menuProductMapper,
            final MenuRepository menuRepository,
            ProductRepository productRepository) {
        this.menuMapper = menuMapper;
        this.menuProductMapper = menuProductMapper;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CreateMenuResponse create(final CreateMenuRequest request) {
        final Menu menu = menuMapper.toMenu(request);
        List<MenuProduct> savedMenuProducts = getMenuProducts(request, menu);
        Menu updated = menu.updateMenuProducts(savedMenuProducts);
        return CreateMenuResponse.from(menuRepository.save(updated));
    }

    private List<MenuProduct> getMenuProducts(CreateMenuRequest request, Menu menu) {
        List<MenuProduct> savedMenuProducts = request.getMenuProducts().stream()
                .map(menuProductMapper::toMenuProduct)
                .collect(Collectors.toList());

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct savedMenuProduct : savedMenuProducts) {
            Product product = productRepository.findById(savedMenuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(savedMenuProduct.getQuantity())));
        }
        menu.validPrice(sum);
        return savedMenuProducts;
    }

    public List<MenuResponse> list() {

        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
