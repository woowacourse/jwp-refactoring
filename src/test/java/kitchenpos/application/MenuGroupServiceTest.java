package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 저장한다.")
    @Test
    void save() {
        // given
        final MenuGroup menuGroup = new MenuGroup("추천메뉴");

        // when, then
        final List<MenuGroup> all = menuGroupService.findAll();
        for (MenuGroup group : all) {
            System.out.println(group.getName());
        }
        assertThat(menuGroupService.create(menuGroup)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new MenuGroup("추천메뉴"));
    }

    @DisplayName("메뉴 그룸의 이름이 null인 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifNameIsNull() {
        // given
        final MenuGroup menuGroup = new MenuGroup();

        // when, then
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 null일 수 없습니다.");
    }

    @DisplayName("메뉴 그룹을 전체 조회한다.")
    @Test
    void findAll() {
        // given, when, then
        assertThat(menuGroupService.findAll()).hasSize(4);
    }
}
