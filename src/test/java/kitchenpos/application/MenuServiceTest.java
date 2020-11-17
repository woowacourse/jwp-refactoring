package kitchenpos.application;

import kitchenpos.domain.menu.*;
import kitchenpos.dto.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final Long 메뉴_ID_1 = 1L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");
    private static final String 상품_후라이드_치킨 = "후라이드 치킨";
    private static final String 상품_코카콜라 = "코카콜라";
    private static final Long 상품_1개 = 1L;
    private static final Long 상품_2개 = 2L;
    private static final Long 상품_ID_1 = 1L;
    private static final Long 상품_ID_2 = 2L;
    private static final BigDecimal 상품_가격_15000원 = new BigDecimal("15000.0");
    private static final BigDecimal 상품_가격_1000원 = new BigDecimal("1000.0");


    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuProductService menuProductService;

    private MenuService menuService;
    private List<ProductQuantityRequest> productQuantityRequests;
    private List<Product> products;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository, menuProductService);

        productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개),
                new ProductQuantityRequest(상품_ID_2, 상품_2개)
        );
        products = Arrays.asList(
                new Product(상품_ID_1, 상품_후라이드_치킨, 상품_가격_15000원),
                new Product(상품_ID_2, 상품_코카콜라, 상품_가격_1000원)
        );
        menuGroup = new MenuGroup(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
    }

    @DisplayName("Menu 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        MenuCreateRequest request = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, 메뉴_그룹_ID_1, productQuantityRequests);
        Menu menu = new Menu(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup);
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);
        when(productRepository.findAllById(anyList())).thenReturn(products);
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(menuGroup));
        doNothing().when(menuProductService).createMenuProducts(any(Menu.class), any(ProductQuantityRequests.class));

        MenuResponse menuResponse = menuService.create(request);

        assertThat(menuResponse.getId()).isEqualTo(메뉴_ID_1);
        assertThat(menuResponse.getName()).isEqualTo(메뉴_이름_후라이드_치킨);
        assertThat(menuResponse.getPrice()).isEqualTo(메뉴_가격_16000원);
        assertThat(menuResponse.getMenuGroup().getId()).isEqualTo(메뉴_그룹_ID_1);
        assertThat(menuResponse.getMenuGroup().getName()).isEqualTo(메뉴_그룹_이름_후라이드_세트);
        assertThat(menuResponse.getProducts()).
                hasSize(2).
                extracting("id").
                containsOnly(상품_ID_1, 상품_ID_2);
    }

    @DisplayName("예외 테스트 : Menu 생성 중 가격이 0 미만일 경우, 예외가 발생한다.")
    @Test
    void createWithNegativePriceExceptionTest() {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        MenuCreateRequest request = new MenuCreateRequest(메뉴_이름_후라이드_치킨, invalidPrice, 메뉴_그룹_ID_1, productQuantityRequests);

        when(productRepository.findAllById(anyList())).thenReturn(products);
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(menuGroup));

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 메뉴 가격이 입력되었습니다.");
    }

    @DisplayName("예외 테스트 : Menu 생성 중 가격이 NULL일 경우, 예외가 발생한다.")
    @Test
    void createWithNullPriceExceptionTest() {
        MenuCreateRequest request = new MenuCreateRequest(메뉴_이름_후라이드_치킨, null, 메뉴_그룹_ID_1, productQuantityRequests);

        when(productRepository.findAllById(anyList())).thenReturn(products);
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(menuGroup));

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("예외 테스트 : Menu 생성 중 MenuGroup을 DB에서 찾을 수 없는 경우, 예외가 발생한다.")
    @Test
    void createWithInvalidMenuGroupIdExceptionTest() {
        Long invalidMenuGroupId = -1L;
        MenuCreateRequest request = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, invalidMenuGroupId, productQuantityRequests);

        when(productRepository.findAllById(anyList())).thenReturn(products);
        when(menuGroupRepository.findById(invalidMenuGroupId)).thenThrow(new IllegalArgumentException("해당 메뉴 그룹을 찾을 수 없습니다."));

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 메뉴 그룹을 찾을 수 없습니다.");
    }

    @DisplayName("예외 테스트 : Menu 생성 중 존재하지 않는 상품이 MenuProduct에 포함될 경우, 예외가 발생한다.")
    @Test
    void createWithInvalidProductExceptionTest() {
        MenuCreateRequest request = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, 메뉴_그룹_ID_1, productQuantityRequests);

        when(productRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 정보가 올바르지 않습니다.");
    }

    @DisplayName("예외 테스트 : Menu 생성 중 모든 상품 가격의 합이 메뉴 가격보다 낮은 경우, 예외가 발생한다.")
    @Test
    void createWithInvalidPriceSumExceptionTest() {
        BigDecimal invalidPrice = 메뉴_가격_16000원.add(BigDecimal.valueOf(10000L));
        MenuCreateRequest request = new MenuCreateRequest(메뉴_이름_후라이드_치킨, invalidPrice, 메뉴_그룹_ID_1, productQuantityRequests);

        when(productRepository.findAllById(anyList())).thenReturn(products);
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(menuGroup));

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 메뉴 가격이 입력되었습니다.");
    }

    @DisplayName("Menu 전체 목록을 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        List<Menu> menus = Arrays.asList(new Menu(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup));
        List<ProductResponse> productResponses = Arrays.asList(
                new ProductResponse(상품_ID_1, 상품_후라이드_치킨, 상품_가격_15000원),
                new ProductResponse(상품_ID_2, 상품_코카콜라, 상품_가격_1000원)
        );
        when(menuRepository.findAll()).thenReturn(menus);
        when(menuProductService.findProductsByMenu(any(Menu.class))).thenReturn(productResponses);

        List<MenuResponse> menuResponses = menuService.list();

        assertThat(menuResponses).hasSize(1);
        assertThat(menuResponses.get(0).getId()).isEqualTo(메뉴_ID_1);
        assertThat(menuResponses.get(0).getName()).isEqualTo(메뉴_이름_후라이드_치킨);
        assertThat(menuResponses.get(0).getPrice()).isEqualTo(메뉴_가격_16000원);
        assertThat(menuResponses.get(0).getMenuGroup().getId()).isEqualTo(메뉴_그룹_ID_1);
        assertThat(menuResponses.get(0).getMenuGroup().getName()).isEqualTo(메뉴_그룹_이름_후라이드_세트);
        assertThat(menuResponses.get(0).getProducts()).
                hasSize(2).
                extracting("id").
                containsOnly(상품_ID_1, 상품_ID_2);

    }
}
