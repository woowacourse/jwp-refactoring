package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
    }

    @DisplayName("메뉴 그룹을 저장한다.")
    @Test
    void save() {
        // given
        final MenuGroup menuGroupRequest = createMenuGroupRequest("추천메뉴");

        // when, then
        assertThat(menuGroupService.create(menuGroupRequest)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(createMenuGroup("추천메뉴"));
    }

    @DisplayName("메뉴 그룸의 이름이 null인 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifNameIsNull() {
        // given
        final MenuGroup menuGroupRequest = createMenuGroupRequest(null);

        // when, then
        assertThatThrownBy(() -> menuGroupService.create(menuGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 null일 수 없습니다.");
    }

    @DisplayName("메뉴 그룹을 전체 조회한다.")
    @Test
    void findAll() {
        // given
        menuGroupDao.save(createMenuGroup("추천메뉴"));

        // when, then
        assertThat(menuGroupService.findAll()).hasSize(1);
    }

    private MenuGroup createMenuGroupRequest(final String name) {
        final MenuGroup menuGroupRequest = new MenuGroup();
        menuGroupRequest.setName(name);
        return menuGroupRequest;
    }
}
