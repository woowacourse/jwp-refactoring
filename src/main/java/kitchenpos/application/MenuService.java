package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuProductRequestDto;
import kitchenpos.application.dto.request.MenuRequestDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        MenuRepository menuRepository,
        MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponseDto create(final MenuRequestDto menuRequestDto) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequestDto.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException("Menu가 속한 MenuGroup이 존재하지 않습니다."));
        Menu menu = new Menu(menuRequestDto.getName(), menuRequestDto.getPrice(), menuGroup);

        List<MenuProduct> menuProducts = menuRequestDto.getMenuProductRequestDtos()
            .stream()
            .map(menuProductRequestDto -> convertToMenuProduct(menuProductRequestDto, menu))
            .collect(Collectors.toList());
        menu.updateMenuProducts(menuProducts);

        return MenuResponseDto.from(menuRepository.save(menu));
    }

    private MenuProduct convertToMenuProduct(MenuProductRequestDto menuProductRequestDto, Menu menu) {
        Product product = productRepository.findById(menuProductRequestDto.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("Product가 존재하지 않습니다."));
        return new MenuProduct(menu, product, menuProductRequestDto.getQuantity());
    }

    public List<MenuResponseDto> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponseDto::from)
            .collect(Collectors.toList());
    }
}
