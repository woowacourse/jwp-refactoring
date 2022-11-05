package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴를 추가할 수 있다.")
    void create() {
        final MenuDto menu1 = 메뉴_등록("런치세트", 15000L, 세트, 토마토파스타.getId(), 탄산음료.getId());
        final MenuDto menu2 = 메뉴_등록("목살스테이크", 20000L, 스테이크, 목살스테이크.getId());

        final List<MenuDto> findMenus = 메뉴_전체_조회();
        final MenuDto findMenu1 = 메뉴_찾기(menu1.getId());
        final MenuDto findMenu2 = 메뉴_찾기(menu2.getId());

        assertAll(
                () -> assertThat(findMenus).usingElementComparatorIgnoringFields("menuProducts")
                        .contains(menu1, menu2),
                () -> assertThat(findMenu1.getMenuProducts()).extracting("productId")
                        .containsExactly(토마토파스타.getId(), 탄산음료.getId()),
                () -> assertThat(findMenu2.getMenuProducts()).extracting("productId")
                        .containsExactly(목살스테이크.getId())
        );
    }

    @Test
    @DisplayName("메뉴의 가격은 null일 수 없다.")
    void createWithNullPrice() {
        assertThatThrownBy(() -> 메뉴_등록("런치세트", null, 세트, 토마토파스타.getId(), 탄산음료.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 0보다 작을 수 없다.")
    void createWithUnderZeroPrice() {
        assertThatThrownBy(() -> 메뉴_등록("런치세트", -1L, 세트, 토마토파스타.getId(), 탄산음료.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 이름은 null일 수 없다.")
    void createWithNullName() {
        assertThatThrownBy(() -> 메뉴_등록(null, 15000L, 세트, 토마토파스타.getId(), 탄산음료.getId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("등록되는 메뉴는 메뉴 그룹에 반드시 포함되어야 한다.")
    void createWithNullMenuGroup() {
        final MenuGroup emptyMenuGroup = new MenuGroup(-1L, "없는그룹");

        assertThatThrownBy(() -> 메뉴_등록("런치세트", 15000L, emptyMenuGroup, 토마토파스타.getId(), 탄산음료.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 등록할 때 메뉴에 포함되는 메뉴상품은 모두 상품목록에 존재해야한다.")
    void createWithNotExistProduct() {
        assertThatThrownBy(() -> 메뉴_등록("런치세트", 15000L, 세트, -1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 메뉴 상품의 가격 합보다 더 클 수 없다.")
    void createWithOverPrice() {
        assertThatThrownBy(() -> 메뉴_등록("런치세트", 19001L, 세트, 토마토파스타.getId(), 탄산음료.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴가 등록될 때 메뉴 상품도 같이 등록한다.")
    void createWithMenuProducts() {
        final MenuDto menu = 메뉴_등록("런치세트", 15000L, 세트, 토마토파스타.getId(), 탄산음료.getId());

        final List<MenuProductDto> menuProducts = menu.getMenuProducts();

        assertThat(menuProducts).extracting("productId")
                .contains(토마토파스타.getId(), 탄산음료.getId());
    }
}
