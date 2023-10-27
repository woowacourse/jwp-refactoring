package kitchenpos.application;

import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.mapper.MenuMapper;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.product.Product;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        final MenuProducts menuProducts = makeMenuProducts(request);
        validateMenuPrice(request, menuProducts);

        final Menu menu = new Menu(request.getName(), new Price(request.getPrice()), menuGroup, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);

        return MenuMapper.mapToMenuResponse(savedMenu);
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private MenuProducts makeMenuProducts(final MenuCreateRequest request) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductDto menuProductDto : request.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(product, menuProductDto.getQuantity()));
        }

        return new MenuProducts(menuProducts);
    }

    private void validateMenuPrice(MenuCreateRequest request, MenuProducts menuProducts) {
        if (menuProducts.checkMenuProductsPriceIsMoreThan(new Price(request.getPrice()))) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> savedMenu = menuRepository.findAll();

        return savedMenu.stream()
                .map(MenuMapper::mapToMenuResponse)
                .collect(Collectors.toList());
    }
}
