package kitchenpos.support.fixture;

public enum MenuFixtures {

    후라이드메뉴(1L, "후라이드", 16000),
    양념치킨메뉴(2L, "양념치킨", 16000),
    반반치킨메뉴(3L, "반반치킨", 16000)
    ;

    private final long id;
    private final String name;
    private final int price;

    MenuFixtures(final long id, final String name, final int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
