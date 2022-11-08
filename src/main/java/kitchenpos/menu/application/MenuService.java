package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.dto.service.MenuProductCreateDto;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {

        if (request.getMenuGroupId() == null) {
            throw new IllegalArgumentException();
        }

        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);

        List<MenuProductCreateDto> menuProducts = request.getMenuProducts().stream()
            .map(this::convertMenuProductRequestToDto)
            .collect(Collectors.toUnmodifiableList());

        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);

        return menuRepository.save(menu);
    }

    private MenuProductCreateDto convertMenuProductRequestToDto(MenuProductCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(IllegalArgumentException::new);

        return new MenuProductCreateDto(product, request.getQuantity());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
