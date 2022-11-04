package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuCreationDto;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.dto.request.MenuCreationRequest;
import kitchenpos.menu.ui.dto.request.MenuProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("MenuService 는 ")
@SpringTestWithData
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("productName", BigDecimal.valueOf(1000L)));
        final List<MenuProductRequest> menuProductsRequest = List.of(new MenuProductRequest(product.getId(), 2));
        final MenuCreationRequest menuCreationRequest = new MenuCreationRequest("menuName", BigDecimal.valueOf(1500L),
                menuGroup.getId(),
                menuProductsRequest);
        final MenuDto menuDto = menuService.create(MenuCreationDto.from(menuCreationRequest));

        assertAll(
                () -> assertThat(menuDto.getId()).isGreaterThanOrEqualTo(1L),
                () -> assertThat(menuDto.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void getMenus() {
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("productName", BigDecimal.valueOf(1000L)));
        final List<MenuProductRequest> menuProductsRequest = List.of(new MenuProductRequest(product.getId(), 2));
        final MenuCreationRequest menuCreationRequest = new MenuCreationRequest("menuName", BigDecimal.valueOf(1500L),
                menuGroup.getId(),
                menuProductsRequest);
        final MenuDto menuDto = menuService.create(MenuCreationDto.from(menuCreationRequest));

        final List<MenuDto> menuDtos = menuService.getMenus();

        assertAll(
                () -> assertThat(menuDtos.size()).isEqualTo(1),
                () -> assertThat(menuDtos.get(0).getId()).isEqualTo(menuDto.getId())
        );
    }
}
