package strata.persistent;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestPersistentMedium {

	public void setUp() {
		
	}
	
	@Test
	public void test() {
		
		PersistentOutputMedium out = new ASCIIFileOutputMedium("test");
		
		out.open("yellow");
		for(int i = 0; i < 10; i++) {
			out.write(i);
		}
		out.close();

		PersistentInputMedium in = new ASCIIFileInputMedium("test");
		
		in.open("yellow");
		for(int i = 0; i < 10; i++) {
			assertEquals(Integer.valueOf(i), in.nextInteger());
		}
		
		assertFalse(in.hasNext());
		
		in.close();
	}

}
