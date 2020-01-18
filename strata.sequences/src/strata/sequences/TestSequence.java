package strata.sequences;

import static org.junit.Assert.*;

import org.junit.Test;

import strata.sequences.iface.Sequence;
import strata.sequences.iface.SequenceFactory;

public class TestSequence {

	private SequenceFactory sequenceFactory;

	public TestSequence() {
		sequenceFactory = SegmentedSequenceFactory.instance();
	}
	
	@Test
	public void test() {
		Sequence aSequence = sequenceFactory.createSequence();

		for(int i = 0; i < 10; i++)
			aSequence.append(i);

		assertEquals((Integer)10,  aSequence.size());

		for(int i = 0; i < 10; i++)
			assertEquals((Integer)i, aSequence.at(i));
	}
	
	@Test
	public void testIO() {
		
		Sequence aSequence = sequenceFactory.createSequence();
		
		for(int i = 0; i < 10; i++)
			aSequence.append(i);
		
		aSequence.fileOut();
		
		Sequence anotherSequence = sequenceFactory.recreateSequenceWithId(aSequence.id());

		assertEquals(aSequence.size(), anotherSequence.size());
		
		for(int i = 0; i < 10; i++)
			assertEquals(aSequence.at(i), anotherSequence.at(i));
	}

	@Test
	public void testCutAndPaste() {

		Sequence aSequence = sequenceFactory.createSequence();
		
		for(int i = 0; i < 10; i++)
			aSequence.append(i);
		
		aSequence.cut(3, 5);
		
		assertEquals(Integer.valueOf(8), aSequence.size());
		
		for(int i = 0; i < 3; i++)
			assertEquals(Integer.valueOf(i), aSequence.at(i));
		
		for(int i = 3; i < 8; i++)
			assertEquals(Integer.valueOf(i+2), aSequence.at(i));
	}
}
