package kitchenpos.dao.entity;

public class MenuGroupEntity {

  private final Long id;
  private final String name;

  public MenuGroupEntity(final Long id, final String name) {
    this.id = id;
    this.name = name;
  }

  public MenuGroupEntity(final String name) {
    this(null, name);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
