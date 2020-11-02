package kitchenpos.menu.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuRequestDto;
import kitchenpos.menu.dto.MenuResponses;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

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
    public Long create(final MenuCreateRequest request) {
        List<MenuRequestDto> menuRequestDtos = request.getMenuProductRequest().stream()
            .map(req -> new MenuRequestDto(findProduct(req.getProductId()), req.getQuantity()))
            .collect(Collectors.toList());

        BigDecimal sum = menuRequestDtos.stream()
            .map(requestDto -> requestDto.getProduct().multiply(requestDto.getQuantity()))
            .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        BigDecimal price = BigDecimal.valueOf(request.getPrice());
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        MenuGroup menuGroup = findMenuGroup(request.getMenuGroupId());
        Menu menu = request.toEntity(menuGroup);

        final Menu savedMenu = menuRepository.save(menu);

        List<MenuProduct> savedMenuProducts = menuRequestDtos.stream()
            .map(x -> new MenuProduct(null, menu, x.getProduct(), x.getQuantity()))
            .map(menuProductRepository::save)
            .collect(Collectors.toList());

        savedMenu.changeMenuProducts(savedMenuProducts);

        return savedMenu.getId();
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(IllegalArgumentException::new);
    }

    public MenuResponses list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponses.from(menus);
    }
}
