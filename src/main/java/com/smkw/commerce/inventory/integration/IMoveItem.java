package com.smkw.commerce.inventory.integration;

import org.springframework.integration.annotation.Gateway;

import com.smkw.commerce.inventory.xml.IMMessage;

public interface IMoveItem {
	@Gateway(requestChannel="items")
	void moveItem(IMMessage item);

}
