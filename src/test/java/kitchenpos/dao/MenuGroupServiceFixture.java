package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class MenuGroupServiceFixture {

    protected MenuGroup 저장된_메뉴_그룹 = new MenuGroup();
    protected MenuGroup 생성할_메뉴_그룹 = new MenuGroup();
    protected List<MenuGroup> 저장된_메뉴_그룹_리스트;

    @BeforeEach
    void setUp() {
        저장된_메뉴_그룹.setName("추천 메뉴");
        저장된_메뉴_그룹.setId(1L);

        생성할_메뉴_그룹.setName("추천 메뉴");

        저장된_메뉴_그룹_리스트 = List.of(저장된_메뉴_그룹, 생성할_메뉴_그룹);
    }
}
