package kitchenpos.fixture;

@SuppressWarnings("NonAsciiCharacters")
public class Fixture {

    public static class Menu {
        public static final Long 후라이드_치킨 = 1L;
        public static final Long 양념_치킨 = 2L;

        public static final Long 없는_메뉴 = 7L;
    }

    public static class MenuGroup {
        public static final Long 두마리_메뉴 = 1L;

        public static final Long 없는_메뉴_그룹 = 5L;
    }

    public static class Product {
        public static final Long 후라이드 = 1L;
        public static final Long 양념치킨 = 2L;
    }

    public static class OrderTable {
        public static final Long 첫번째_테이블 = 1L;
        public static final Long 두번째_테이블 = 2L;
        public static final Long 세번째_테이블 = 3L;

        public static final Long 없는_테이블 = 9L;
    }
}
