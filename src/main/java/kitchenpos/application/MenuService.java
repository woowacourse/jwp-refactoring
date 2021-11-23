package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.MenuDtoAssembler;
import kitchenpos.application.dto.request.MenuProductRequestDto;
import kitchenpos.application.dto.request.MenuRequestDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public MenuResponseDto create(MenuRequestDto requestDto) {
        MenuGroup menuGroup = menuGroupRepository.findById(requestDto.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequestDto menuProduct : requestDto.getMenuProducts()) {
            Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);

            menuProducts.add(new MenuProduct(menuProduct.getQuantity(), product));
        }

        Menu menu = menuRepository
            .save(new Menu(requestDto.getName(), requestDto.getPrice(), menuGroup, menuProducts));

        return MenuDtoAssembler.menuResponseDto(menu);
    }

    public List<MenuResponseDto> list() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuDtoAssembler::menuResponseDto)
            .collect(toList());
    }
}
