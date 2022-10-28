package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.ProductRequest;
import kitchenpos.repository.MenuGroupRepository;

@SpringBootTest
@Transactional
class MenuServiceTest {

    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        menuGroupId = menuGroupService.create(new MenuGroupRequest("test")).getId();

        Long productId = productService.create(new ProductRequest("test", 100)).getId();
        menuProducts = Arrays.asList(new MenuProductRequest(productId, 10));
    }

    @Test
    @DisplayName("Menu를 생성한다.")
    void create() {
        //given
        MenuRequest menuRequest = new MenuRequest("test", 500000, menuGroupId, menuProducts);

        //when
        Menu savedMenu = menuService.create(menuRequest);

        //then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 MenuGroupId면 예외를 발생시킨다.")
    void createWithNotExistMenuGroupIdError() {
        //given, when
        menuGroupRepository.deleteById(menuGroupId);
        MenuRequest menuRequest = new MenuRequest("test", 500000, menuGroupId, menuProducts);

        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {900, 100})
    @DisplayName("개별 상품의 합이 menu 가격의 합보다 클 경우 예외를 발생시킨다.")
    void createWithCheaperPriceError(int price) {
        //when
        MenuRequest menuRequest = new MenuRequest("test", price, menuGroupId, menuProducts);

        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 메뉴목록을 조회한다.")
    void findByList() {
        //given
        List<Menu> menus = menuService.list();

        //when
        MenuRequest menuRequest = new MenuRequest("test", 500000, menuGroupId, menuProducts);

        menuService.create(menuRequest);

        //then
        assertThat(menuService.list()).hasSize(menus.size() + 1);
    }
}
