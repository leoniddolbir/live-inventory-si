package com.smkw.commerce.inventory.test;

import org.junit.Test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.QSYSObjectPathName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CommandCallTest {
	@Test
	public void callCommand() {
		AS400 system = new AS400("10.0.0.9", "WEBSITE", "WEBSITE");
		QSYSObjectPathName path = new QSYSObjectPathName("OPCUS001", "INHSEQ", "DTAARA");
		CharacterDataArea dataArea = new CharacterDataArea(system, path.getPath());
		// Read Character data area on the AS/400.
		try {
			String currentNumber = dataArea.read(0, 11);
			assertNotNull(currentNumber);
			long sequence = Long.parseLong(currentNumber);
			String nextNumber = String.valueOf(sequence + 1);
			// prepend with 0 to match data area lenght
			while (nextNumber.length() < currentNumber.length()) {
				nextNumber = "0" + nextNumber;
			}
			assertEquals(nextNumber.length(), currentNumber.length());
			dataArea.write(nextNumber);
			String storedValue = dataArea.read(0, 11);
			assertEquals(nextNumber, storedValue);
			System.out.println("The Next Value is " + storedValue);
		} catch (Exception e) {
			System.out.println("Command  issued an exception!");
			e.printStackTrace();
		} finally {
			// Done with the server.
			system.disconnectAllServices();
		}
	}
}
