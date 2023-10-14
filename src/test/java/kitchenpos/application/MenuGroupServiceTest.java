package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 저장할 수 있다.")
    @Test
    void createSuccessTest() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("TestMenuGroup");

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        MenuGroup findMenuGroup = menuGroupService.list()
                .stream()
                .filter(group -> group.getId().equals(savedMenuGroup.getId()))
                .findAny()
                .get();

        assertThat(savedMenuGroup)
                .usingRecursiveComparison()
                .isEqualTo(findMenuGroup);
    }

}
