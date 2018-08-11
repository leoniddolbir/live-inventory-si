package com.smkw.commerce.inventory.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smkw.commerce.inventory.MailerConfigDev;

@ContextConfiguration(classes = { MailerConfigDev.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class StatusNotificationTest {

}
