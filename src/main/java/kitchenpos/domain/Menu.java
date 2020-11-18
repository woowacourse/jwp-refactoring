package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Embedded
	private Money price;
	private Long menuGroupId;
	@OneToMany(mappedBy = "menuId", fetch = FetchType.EAGER)
	private List<MenuProduct> menuProducts;

	protected Menu() {
	}

	public Menu(Long id, String name, Money price, Long menuGroupId,
		List<MenuProduct> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Money getPrice() {
		return price;
	}

	public Long getMenuGroup() {
		return menuGroupId;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void setMenuProducts(List<MenuProduct> savedMenuProducts) {
		this.menuProducts = savedMenuProducts;
	}
}
