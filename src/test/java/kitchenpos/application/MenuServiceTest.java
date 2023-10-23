package kitchenpos.application;

import kitchenpos.EntityFactory;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private EntityFactory entityFactory;

    @Nested
    @DisplayName("메뉴 생성 테스트")
    class CreateTest {
        @Test
        @DisplayName("메뉴를 생성할 수 있다")
        void create() {
            //given
            final Product product = entityFactory.saveProduct("연어", 4000);
            final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 4);
            final MenuGroup menuGroup = entityFactory.saveMenuGroup("일식");

            final MenuCreateRequest request = new MenuCreateRequest("떡볶이 세트", BigDecimal.valueOf(16000),
                    menuGroup.getId(), singletonList(menuProduct));

            //when
            final MenuResponse menu = menuService.create(request);

            //then
            assertThat(menu.getId()).isNotNull();
        }

        @Test
        @DisplayName("메뉴를 생성할 때 메뉴 그룹이 없으면 예외가 발생한다")
        void create_fail1() {
            //given
            final Product product = entityFactory.saveProduct("연어", 4000);
            final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 4);

            final MenuCreateRequest request = new MenuCreateRequest("떡볶이 세트", BigDecimal.valueOf(16000),
                    0L, singletonList(menuProduct));

            //when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴 그룹입니다.");
        }

        @Test
        @DisplayName("메뉴를 생성할 때 실제 금액보다 요청 금액이 크면 예외가 발생한다")
        void create_fail2() {
            final Product product = entityFactory.saveProduct("연어", 4000);
            final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 4);
            final MenuGroup menuGroup = entityFactory.saveMenuGroup("일식");

            final MenuCreateRequest request = new MenuCreateRequest("떡볶이 세트", BigDecimal.valueOf(16001),
                    menuGroup.getId(), singletonList(menuProduct));

            //when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격은 상품 가격들의 합보다 클 수 없습니다.");
        }

        @Test
        @DisplayName("메뉴를 생성할 때 실제 상품이 존재하지 않으면 예외가 발생한다")
        void create_fail3() {
            final Product product = new Product(0L, "연어", new Price(BigDecimal.TEN));
            final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 4);
            final MenuGroup menuGroup = entityFactory.saveMenuGroup("일식");

            final MenuCreateRequest request = new MenuCreateRequest("떡볶이 세트", BigDecimal.valueOf(16000),
                    menuGroup.getId(), singletonList(menuProduct));

            //when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 상품입니다.");
        }
    }

    @Test
    @DisplayName("메뉴 전체 조회할 수 있다")
    void list() {
        assertDoesNotThrow(() -> menuService.list());
    }
}
