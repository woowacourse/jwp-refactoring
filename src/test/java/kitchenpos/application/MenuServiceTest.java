package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.domain.*;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.response.MenuResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("MenuService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 후라이드치킨정보;
    private Product 양념치킨정보;
    private MenuProduct 후라이드치킨;
    private MenuProduct 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨정보 = new Product(1L, "후라이드 치킨", 16000);
        양념치킨정보 = new Product(2L, "양념 치킨", 16000);
        후라이드치킨 = new MenuProduct(후라이드치킨정보, 1);
        양념치킨 = new MenuProduct(양념치킨정보, 1);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다")
    void create() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", BigDecimal.valueOf(30000), 1L, Arrays.asList(후라이드치킨, 양념치킨));
        Menu menu = new Menu(1L, "양념 반 + 후라이드 반", 30000, 1L, Arrays.asList(후라이드치킨, 양념치킨));
        given(menuGroupRepository.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(후라이드치킨.getProduct().getId())).willReturn(Optional.of(후라이드치킨정보));
        given(productRepository.findById(양념치킨.getProduct().getId())).willReturn(Optional.of(양념치킨정보));
        given(menuRepository.save(any(Menu.class))).willReturn(menu);
        MenuProduct expected_후라이드치킨 = new MenuProduct(1L, menu, 후라이드치킨정보, 1);
        MenuProduct expected_양념치킨 = new MenuProduct(2L, menu, 양념치킨정보, 1);
        menu.setMenuProducts(Arrays.asList(expected_후라이드치킨, expected_양념치킨));
        given(menuProductRepository.save(any(MenuProduct.class))).willReturn(expected_후라이드치킨, expected_양념치킨);
        MenuResponse expected = MenuResponse.from(menu);

        // when
        MenuResponse actual = menuService.create(양념반_후라이드반);

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertEquals(2, actual.getMenuProducts().size());
    }

    @Test
    @DisplayName("메뉴의 가격이 null이면 메뉴를 등록할 수 없다.")
    void createWrongPriceNull() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", null, 1L, Arrays.asList(후라이드치킨, 양념치킨));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴의 가격이 음수면 메뉴를 등록할 수 없다.")
    void createWrongPriceUnderZero() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", BigDecimal.valueOf(-1), 1L, Arrays.asList(후라이드치킨, 양념치킨));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴를 구성하는 실제 제품들을 단품으로 주문하였을 때의 가격 합보다 크면 메뉴를 등록할 수 없다.")
    void createWrongPriceSumOfProducts() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", BigDecimal.valueOf(32001), 1L, Arrays.asList(후라이드치킨, 양념치킨));
        given(menuGroupRepository.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(후라이드치킨.getProduct().getId())).willReturn(Optional.of(후라이드치킨정보));
        given(productRepository.findById(양념치킨.getProduct().getId())).willReturn(Optional.of(양념치킨정보));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면 메뉴를 등록할 수 없다.")
    void createWrongMenuGroupNotExist() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", BigDecimal.valueOf(30000), 1L, Arrays.asList(후라이드치킨, 양념치킨));
        given(menuGroupRepository.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(false);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴 그룹이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("목록에 포함된 데이터들이 존재하지 않으면 메뉴를 등록할 수 없다.")
    void createWrongProductNotExist() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", BigDecimal.valueOf(30000), 1L, Arrays.asList(후라이드치킨, 양념치킨));
        given(menuGroupRepository.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(후라이드치킨.getProduct().getId())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("상품이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("전체 메뉴를 조회할 수 있다")
    void list() {
        // given
        Product 간장치킨정보 = new Product(3L, "간장 치킨", 16000);
        MenuProduct 간장치킨 = new MenuProduct(간장치킨정보, 1);
        Menu 양념반_후라이드반 = new Menu(1L, "양념 반 + 후라이드 반", 30000, 1L, Arrays.asList(후라이드치킨, 양념치킨));
        Menu 간장반_후라이드반 = new Menu(2L, "간장 반 + 후라이드 반", 30000, 2L, Arrays.asList(후라이드치킨, 간장치킨));
        given(menuRepository.findAll()).willReturn(Arrays.asList(양념반_후라이드반, 간장반_후라이드반));

        MenuProduct expected_후라이드치킨_menu1 = new MenuProduct(1L, 양념반_후라이드반, 후라이드치킨정보, 1);
        MenuProduct expected_양념치킨 = new MenuProduct(2L, 양념반_후라이드반, 양념치킨정보, 1);
        MenuProduct expected_후라이드치킨_menu2 = new MenuProduct(3L, 간장반_후라이드반, 후라이드치킨정보, 1);
        MenuProduct expected_간장치킨 = new MenuProduct(4L, 간장반_후라이드반, 간장치킨정보, 1);
        양념반_후라이드반.setMenuProducts(Arrays.asList(expected_후라이드치킨_menu1, expected_양념치킨));
        간장반_후라이드반.setMenuProducts(Arrays.asList(expected_후라이드치킨_menu2, expected_간장치킨));
        given(menuProductRepository.findAllByMenu(양념반_후라이드반)).willReturn(Arrays.asList(expected_후라이드치킨_menu1, expected_양념치킨));
        given(menuProductRepository.findAllByMenu(간장반_후라이드반)).willReturn(Arrays.asList(expected_후라이드치킨_menu2, expected_간장치킨));

        MenuResponse expected_양념반_후라이드반 = MenuResponse.from(양념반_후라이드반);
        MenuResponse expected_간장반_후라이드반 = MenuResponse.from(간장반_후라이드반);

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertEquals(2, actual.size());
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(expected_양념반_후라이드반);
        assertThat(actual.get(1)).usingRecursiveComparison().isEqualTo(expected_간장반_후라이드반);
    }
}
