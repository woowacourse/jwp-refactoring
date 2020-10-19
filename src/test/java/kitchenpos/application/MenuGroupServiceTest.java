package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Transactional
    @DisplayName("새로운 MenuGroup을 추가할 수 있다.")
    @Test
    void createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("회식세트메뉴");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertAll(() -> {
            assertThat(savedMenuGroup.getId()).isNotNegative();
            assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
        });
    }

    @DisplayName("예외 테스트: 이름이 없는 MenuGroup을 추가하면 예외가 발생한다.")
    @Test
    void createMenuGroupWithoutName() {
        MenuGroup menuGroup = new MenuGroup();

        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    // TODO: 2020/10/19 초기 데이터에 의존하는 테스트 괜찮은가?
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    @Test
    void findAllMenuGroups() {
        String[] menuGroupNames = {"두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴"};

        List<MenuGroup> list = menuGroupService.list();

        assertAll(() -> {
            assertThat(list).hasSize(4);
            assertThat(list).extracting(MenuGroup::getName).containsOnly(menuGroupNames);
        });
    }
}