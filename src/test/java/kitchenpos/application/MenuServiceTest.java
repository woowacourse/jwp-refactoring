package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.CreateMenuProductDto;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("create 메서드는")
    @Nested
    class CreateTest {

        @Test
        void 생성한_메뉴를_반환한다() {
            CreateMenuDto menu = new CreateMenuDto("후라이드+후라이드", BigDecimal.valueOf(19000), 1L,
                    List.of(new CreateMenuProductDto(1L, 2)));

            MenuDto actual = menuService.create(menu);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo("후라이드+후라이드"),
                    () -> assertThat(actual.getPrice()).isNotNull(),
                    () -> assertThat(actual.getMenuGroupId()).isEqualTo(1L),
                    () -> assertThat(actual.getMenuProducts()).hasSize(1)
            );
        }

        @Test
        void 가격정보가_누락된_경우_예외발생() {
            CreateMenuDto menu = new CreateMenuDto("후라이드+후라이드", null, 1L,
                    List.of(new CreateMenuProductDto(1L, 2)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격이_음수인_경우_예외발생() {
            CreateMenuDto menu = new CreateMenuDto("후라이드+후라이드", BigDecimal.valueOf(-1), 1L,
                    List.of(new CreateMenuProductDto(1L, 2)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상품_id인_경우_예외발생() {
            CreateMenuDto menu = new CreateMenuDto("후라이드+후라이드", BigDecimal.valueOf(19000), 1L,
                    List.of(new CreateMenuProductDto(9999999L, 2)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴_그룹_id인_경우_예외발생() {
            CreateMenuDto menu = new CreateMenuDto("후라이드+후라이드", BigDecimal.valueOf(19000), 9999999L,
                    List.of(new CreateMenuProductDto(1L, 2)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품명이_누락된_경우_예외발생() {
            CreateMenuDto menu = new CreateMenuDto(null, BigDecimal.valueOf(19000), 1L,
                    List.of(new CreateMenuProductDto(1L, 2)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(DataAccessException.class);
        }

        @Test
        void 개별_상품을_따로_판매할_때에_비해_가격이_높은_경우_예외발생() {
            CreateMenuDto menu = new CreateMenuDto("후라이드+후라이드", BigDecimal.valueOf(999999999), 1L,
                    List.of(new CreateMenuProductDto(1L, 2)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴를_구성하는_개별_상품에_대한_정보가_누락된_경우_예외발생() {
            CreateMenuDto menu = new CreateMenuDto("후라이드+후라이드", BigDecimal.valueOf(19000), 1L, List.of());

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void list_메서드는_메뉴_목록을_조회한다() {
        List<MenuDto> menus = menuService.list();

        assertThat(menus).hasSizeGreaterThan(1);
    }
}
