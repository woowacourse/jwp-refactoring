package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        Product savedChicken1 = productRepository.save(new Product("간장치킨", 10000L));
        Product savedChicken2 = productRepository.save(new Product("허니콤보치킨", 15000L));

        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("한마리 메뉴"));

        MenuProductCreateRequest menuProductRequest1 = new MenuProductCreateRequest(savedChicken1.getId(), 2L);
        MenuProductCreateRequest menuProductRequest2 = new MenuProductCreateRequest(savedChicken2.getId(), 3L);

        MenuCreateRequest request = new MenuCreateRequest("간장+허니", 60000L, savedMenuGroup.getId(),
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        Menu savedMenu = menuService.create(request);

        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(60000L));
    }

    @DisplayName("메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.")
    @Test
    void createException3() {
        Long wrongPrice = 30000L;

        Product savedChicken1 = productRepository.save(new Product("간장치킨", 10000L));
        Product savedChicken2 = productRepository.save(new Product("허니콤보치킨", 15000L));

        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("한마리 메뉴"));

        MenuProductCreateRequest menuProductRequest1 = new MenuProductCreateRequest(savedChicken1.getId(), 1L);
        MenuProductCreateRequest menuProductRequest2 = new MenuProductCreateRequest(savedChicken2.getId(), 1L);

        MenuCreateRequest request = new MenuCreateRequest("간장+허니", wrongPrice, savedMenuGroup.getId(),
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 금액의 합(%d)이 메뉴의 가격(%d)보다 작습니다.", 25000L, 30000L);
    }

    @DisplayName("메뉴 그룹 없이 메뉴를 생성할 수 없다.")
    @Test
    void createException4() {
        Product savedChicken1 = productRepository.save(new Product("간장치킨", 10000L));
        Product savedChicken2 = productRepository.save(new Product("허니콤보치킨", 15000L));

        MenuProductCreateRequest menuProductRequest1 = new MenuProductCreateRequest(savedChicken1.getId(), 1L);
        MenuProductCreateRequest menuProductRequest2 = new MenuProductCreateRequest(savedChicken2.getId(), 1L);

        MenuCreateRequest request = new MenuCreateRequest("간장+허니", 25000L, -1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹을 선택해주세요.");
    }

    @DisplayName("전체 메뉴를 조회한다.")
    @Test
    void list() {
        Product savedChicken1 = productRepository.save(new Product("간장치킨", 10000L));
        Product savedChicken2 = productRepository.save(new Product("허니콤보치킨", 15000L));

        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("한마리 메뉴"));

        MenuProductCreateRequest menuProductRequest1 = new MenuProductCreateRequest(savedChicken1.getId(), 2L);
        MenuProductCreateRequest menuProductRequest2 = new MenuProductCreateRequest(savedChicken2.getId(), 3L);

        MenuCreateRequest request = new MenuCreateRequest("간장+허니", 25000L, savedMenuGroup.getId(),
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        menuService.create(request);
        menuService.create(request);

        List<Menu> menus = menuRepository.findAll();

        assertThat(menus).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        menuGroupRepository.deleteAll();
        productRepository.deleteAll();
    }
}