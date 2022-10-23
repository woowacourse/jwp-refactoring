package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("MenuGroup을 생성한다.")
    void create() {
        //given
        MenuGroup menuGroup = new MenuGroup("test");

        //when
        MenuGroup saved = menuGroupService.create(menuGroup);

        //then
        assertThat(saved.getId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("리스트 형태로 조회한다.")
    void findAsList() {
        //when
        List<MenuGroup> result = menuGroupService.list();

        //then
        assertAll(
            () -> assertThat(result).hasSize(5)
        );
    }
}
