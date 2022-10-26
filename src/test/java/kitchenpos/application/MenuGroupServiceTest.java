package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.MenuGroup;
import kitchenpos.support.RollbackExtension;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        final String name = "신제품";
        //when
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup(name));

        //then
        assertAll(
                () -> assertThat(menuGroup.getId()).isNotNull(),
                () -> assertThat(menuGroup.getName()).isEqualTo(name)
        );
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void list() {
        //given
        menuGroupService.create(new MenuGroup("신제품"));

        //when
        //then
        assertThat(menuGroupService.list()).hasSize(1);
    }
}
