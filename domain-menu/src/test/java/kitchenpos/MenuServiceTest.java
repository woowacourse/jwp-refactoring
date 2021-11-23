package kitchenpos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.MenuValidator;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.MenuFixture.더블간장;
import static kitchenpos.MenuFixture.양념반_후라이드반;
import static kitchenpos.MenuProductFixture.강정치킨_한마리_메뉴상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@DisplayName("MenuService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 등록할 수 있다")
    void create() {
        // given
        CreateMenuRequest request = new CreateMenuRequest(
                "강정",
                BigDecimal.valueOf(15000),
                1L,
                Collections.singletonList(new MenuProductRequest(1L, 1))
        );
        Menu expected = new Menu(1L, "강정", BigDecimal.valueOf(15000), 1L, Collections.singletonList(강정치킨_한마리_메뉴상품));
        given(menuRepository.save(any(Menu.class))).willReturn(expected);

        // when
        MenuResponse actual = menuService.create(request);

        // then
        assertNotNull(actual.getId());
        assertThat(actual.getName()).isEqualTo("강정");
        assertEquals(1, actual.getMenuProducts().size());
    }

    @Test
    @DisplayName("메뉴의 가격이 null이면 메뉴를 등록할 수 없다.")
    void createWrongPriceNull() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest(
                "양념 반 + 후라이드 반",
                null,
                1L,
                Arrays.asList(
                        new MenuProductRequest(1L, 2),
                        new MenuProductRequest(2L, 1)
                )
        );

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴의 가격이 음수면 메뉴를 등록할 수 없다.")
    void createWrongPriceUnderZero() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest(
                "양념 반 + 후라이드 반",
                BigDecimal.valueOf(-1),
                1L,
                Arrays.asList(
                        new MenuProductRequest(1L, 2),
                        new MenuProductRequest(2L, 1)
                )
        );

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴를 구성하는 실제 제품들을 단품으로 주문하였을 때의 가격 합보다 크면 메뉴를 등록할 수 없다.")
    void createWrongPriceSumOfProducts() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest(
                "양념 반 + 후라이드 반",
                BigDecimal.valueOf(34001),
                1L,
                Arrays.asList(
                        new MenuProductRequest(1L, 1),
                        new MenuProductRequest(2L, 1)
                )
        );
        doThrow(new IllegalArgumentException("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다."))
                .when(menuValidator).validatePrice(anyList(), any());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면 메뉴를 등록할 수 없다.")
    void createWrongMenuGroupNotExist() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest(
                "양념 반 + 후라이드 반",
                BigDecimal.valueOf(30000),
                1L,
                Arrays.asList(
                        new MenuProductRequest(1L, 2),
                        new MenuProductRequest(2L, 1)
                )
        );
        doThrow(new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."))
                .when(menuValidator).validateMenuGroup(anyLong());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴 그룹이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("목록에 포함된 데이터들이 존재하지 않으면 메뉴를 등록할 수 없다.")
    void createWrongProductNotExist() {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest(
                "양념 반 + 후라이드 반",
                BigDecimal.valueOf(32001),
                1L,
                Arrays.asList(
                        new MenuProductRequest(1L, 2),
                        new MenuProductRequest(2L, 1)
                )
        );
        doThrow(new IllegalArgumentException("상품이 존재하지 않습니다."))
                .when(menuValidator).validatePrice(anyList(), any());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("상품이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("전체 메뉴를 조회할 수 있다")
    void list() {
        // given
        given(menuRepository.findAll()).willReturn(Arrays.asList(양념반_후라이드반, 더블간장));

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertEquals(2, actual.size());
    }
}
