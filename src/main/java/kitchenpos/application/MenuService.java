package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.service.MenuProductCreateDto;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
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
