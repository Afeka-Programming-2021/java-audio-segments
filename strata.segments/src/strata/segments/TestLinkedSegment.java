package strata.segments;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import strata.pages.iface.Page;
import strata.segments.iface.Segment;
import strata.segments.iface.SegmentFactory;

class TestLinkedSegment { 

	@Test
	void test() throws FileNotFoundException {
		
		Segment aSegment = SegmentFactory.instance().createSegment();
		
		for(int i = 0 ; i < 10; i++)
			aSegment.append(i);
		
		for(int i = 0 ; i < 10; i++)
			assertEquals(i, aSegment.at(i));
				
		aSegment.fileOut();
		
		Segment anotherSegment = SegmentFactory.instance().recreateSegmentWithId(aSegment.id());
		
		anotherSegment.fileIn();
		
		assertEquals(aSegment.size(), anotherSegment.size());
		
		for(int i = 0; i < 10; i++) {
			assertEquals(aSegment.at(i), anotherSegment.at(i));
		}
	}

	@Test
	void testSplitAtNonePageBoundary() throws FileNotFoundException {
		
		// create the dummy head and tail segments.
		
		LinkedSegment head = new LinkedSegment();
		
		LinkedSegment tail = new LinkedSegment();
		
		// create a segment and split it 
		
		LinkedSegment aSegment = LinkedSegmentFactory.instance().createSegment();
		
		head.linkTo(aSegment);
		
		aSegment.linkTo(tail);
		
		for(int i = 0 ; i < 10; i++)
			aSegment.append(i);
		
		LinkedSegment anotherSegment = aSegment.splitAt(Page.PAGE_SIZE + 1); 
		
		int i = 0;
		for(; i < Page.PAGE_SIZE + 1; i++)
			assertEquals(anotherSegment.at(i), i);
		
		for(; i < Page.PAGE_SIZE * 2; i++)
			assertEquals(anotherSegment.next().at(i - Page.PAGE_SIZE - 1), i);
			
		for(; i < 10; i++)
			assertEquals(anotherSegment.next().next().at(i - Page.PAGE_SIZE * 2), i);

	}

	@Test
	void testSplitAtBeginning() throws FileNotFoundException {
		
		// create the dummy head and tail segments.
		
		LinkedSegment head = new LinkedSegment();
		
		LinkedSegment tail = new LinkedSegment();
		
		// create a segment and split it 
		
		LinkedSegment aSegment = LinkedSegmentFactory.instance().createSegment();
		
		head.linkTo(aSegment);
		
		aSegment.linkTo(tail);
		
		for(int i = 0 ; i < 10; i++)
			aSegment.append(i);
		
		LinkedSegment anotherSegment = aSegment.splitAt(0);
		
		assertEquals(anotherSegment, aSegment);
	}

	@Test
	void testSplitAtPageBoundary() throws FileNotFoundException {
		
		// create the dummy head and tail segments.
		
		LinkedSegment head = new LinkedSegment();
		
		LinkedSegment tail = new LinkedSegment();
		
		// create a segment and split it 
		
		LinkedSegment aSegment = LinkedSegmentFactory.instance().createSegment();
		
		head.linkTo(aSegment);
		
		aSegment.linkTo(tail);
		
		for(int i = 0 ; i < 10; i++)
			aSegment.append(i);
		
		LinkedSegment anotherSegment = aSegment.splitAt(Page.PAGE_SIZE); 
		
		int i = 0;
		
		for(; i < Page.PAGE_SIZE; i++)
			assertEquals(anotherSegment.at(i), i);
		
		for(; i < 10; i++)
			assertEquals(anotherSegment.next().at(i - Page.PAGE_SIZE), i);
	}
}
