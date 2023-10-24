package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    private static final String 메뉴_그룹명 = "메뉴 그룹";

    private static MenuGroup 메뉴_그룹_생성(@Nullable final Integer number) {
        String 메뉴_그룹_이름 = 메뉴_그룹명;

        if (number != null) {
            메뉴_그룹_이름 = 메뉴_그룹_이름 + number;
        }

        return new MenuGroup(메뉴_그룹_이름);
    }

    public static MenuGroup 메뉴_그룹_생성() {
        return 메뉴_그룹_생성(1);
    }

    public static List<MenuGroup> 메뉴_그룹들_생성(final int count) {
        final List<MenuGroup> 메뉴_그룹들 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            메뉴_그룹들.add(메뉴_그룹_생성(i));
        }

        return 메뉴_그룹들;
    }
}
