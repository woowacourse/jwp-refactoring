package fixture;

public enum MenuFixtures {

    후라이드치킨_메뉴(1L, "후라이드치킨", 16000, 2L),
    양념치킨_메뉴(2L, "양념치킨", 16000, 2L),
    반반치킨_메뉴(3L, "반반치킨", 16000, 2L),
    통구이_메뉴(4L, "통구이", 16000, 2L),
    간장치킨_메뉴(5L, "간장치킨", 17000, 2L),
    순살치킨_메뉴(6L, "순살치킨", 17000, 2L);

    private final long id;
    private final String name;
    private final int price;
    private final long menuGroupId;

    MenuFixtures(long id, String name, int price, long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public long id() {
        return id;
    }

    public String 이름() {
        return name;
    }

    public int 가격() {
        return price;
    }

    public long 그룹_ID() {
        return menuGroupId;
    }
}
