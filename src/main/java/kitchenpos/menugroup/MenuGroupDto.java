package kitchenpos.menugroup;

import java.util.Objects;

public class MenuGroupDto {
    private Long id;
    private String name;

    public static MenuGroupDto from(MenuGroup entity) {
        MenuGroupDto menuGroupDto = new MenuGroupDto();
        menuGroupDto.setId(entity.getId());
        menuGroupDto.setName(entity.getName());
        return menuGroupDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MenuGroupDto menuGroupDto = (MenuGroupDto) object;
        return Objects.equals(id, menuGroupDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
