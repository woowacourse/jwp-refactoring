package kitchenpos.application;

import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.response.MenuProductResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuProductRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        final Menu menu = new Menu(request.getName(), new Price(request.getPrice()), menuGroup);
        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(makeMenuProducts(request, savedMenu));

        return new MenuResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice().getValue(), savedMenu.getMenuGroup().getId(),
                savedMenuProducts.stream()
                        .map(menuProduct -> new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(),
                                menuProduct.getProduct().getId(), menuProduct.getQuantity()))
                        .collect(Collectors.toList()));
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> makeMenuProducts(final MenuCreateRequest request, final Menu menu) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductDto menuProductDto : request.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(menu, product, menuProductDto.getQuantity()));
        }

        checkMenuProductsPrice(menuProducts, menu.getPrice());
        return menuProducts;
    }

    private void checkMenuProductsPrice(final List<MenuProduct> menuProducts, final Price price) {
        Price sum = new Price(BigDecimal.ZERO);
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        if (price.isMoreThan(sum)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> savedMenu = menuRepository.findAll();

        return savedMenu.stream()
                .map(menu -> {
                    final List<MenuProduct> savedMenuProducts = menuProductRepository.findAllByMenuId(menu.getId());
                    return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getValue(), menu.getMenuGroup().getId(),
                            savedMenuProducts.stream()
                                    .map(menuProduct -> new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(),
                                            menuProduct.getProduct().getId(), menuProduct.getQuantity()))
                                    .collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
    }
}
