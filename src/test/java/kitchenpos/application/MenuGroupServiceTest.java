package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.inmemory.InmemoryMenuGroupDao;
import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(new InmemoryMenuGroupDao());
    }

    @DisplayName("create: 메뉴 그룹 생성 요청시, 입력 받은 이름을 기반으로 생성 하면, ID 생성 및 입력 값을 통해 생성된다.")
    @Test
    void create() {
        MenuGroup 추가하고자하는메뉴그룹 = menuGroupService.create(createMenuGroup("세트 그룹"));

        assertAll(
                () -> assertThat(추가하고자하는메뉴그룹.getId()).isNotNull(),
                () -> assertThat(추가하고자하는메뉴그룹.getName()).isEqualTo("세트 그룹")
        );
    }

    @DisplayName("list: 현재 저장 되어 있는 메뉴그룹의 목록을 반환한다.")
    @Test
    void list() {
        menuGroupService.create(createMenuGroup("치킨세트"));
        menuGroupService.create(createMenuGroup("치킨단품"));

        List<MenuGroup> 전체메뉴그룹목록 = menuGroupService.list();

        assertThat(전체메뉴그룹목록).hasSize(2);
    }
}