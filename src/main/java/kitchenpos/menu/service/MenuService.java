package kitchenpos.menu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴그룹을 찾을수 없습니다."));
        final List<MenuProductRequest> menuProductDtos = menuRequest.getMenuProducts();
        final List<MenuProduct> menuProducts = menuProductDtos.stream()
                .map(menuProductRequest -> {
                    final Product product = productRepository.findById(menuProductRequest.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 메뉴 상품을 찾을수 없습니다."));
                    return menuProductRequest.toEntity(product);
                }).collect(Collectors.toList());
        final Menu menu = menuRequest.toEntity(menuGroup, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);

        savedMenu.validateByPriceWithMenuProductSum();

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return MenuResponse.ofList(menus);
    }
}
