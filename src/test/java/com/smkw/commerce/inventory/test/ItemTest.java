package com.smkw.commerce.inventory.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.smkw.commerce.inventory.api.data.Flag;
import com.smkw.commerce.inventory.api.data.ItemBrand;
import com.smkw.commerce.inventory.api.data.ItemCategory;
import com.smkw.commerce.inventory.api.data.ItemPriceRange;
import com.smkw.commerce.inventory.api.data.WebItem;
import com.smkw.commerce.inventory.test.config.InfrastructureTestsConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureTestsConfig.class })
public class ItemTest {
	@PersistenceContext
	private EntityManager em;

	@Test
	@Transactional(readOnly = true)
	public void fetchWebItem() {
		WebItem anItem = em.find(WebItem.class, "NV222          ");
		assertNotNull(anItem);
		assertEquals(anItem.getAvailable(), Flag.Y);
		assertTrue(!anItem.getTitle().isEmpty());
		for (ItemPriceRange aPrice : anItem.getPrices()) {
			assertNotNull(aPrice);
			assertEquals(aPrice.getRangeQuantity(), 1);
			assertTrue(aPrice.getUnitPrice().doubleValue() > 1d);
		}
		ItemBrand aBrand = anItem.getBrand();
		assertNotNull(aBrand);
		assertEquals(aBrand.getId(), 81);
		assertTrue(aBrand.getPopular().equals(Flag.Y));
		List<ItemCategory> allCats = anItem.getCategories();
		assertNotNull(allCats);
		assertTrue(allCats.size() > 1);
		List<String> states = anItem.getRestrictingStates();
		assertNotNull(states);
		assertTrue(!states.isEmpty());
		assertTrue(states.size() > 1);

	}

}
