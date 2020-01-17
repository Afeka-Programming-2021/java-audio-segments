package strata.segments;

import java.util.UUID;

import strata.segments.iface.Segment;
import strata.segments.iface.SegmentFactory;

public class LinkedSegmentFactory implements SegmentFactory {

	private static LinkedSegmentFactory instance;
	
	public static LinkedSegmentFactory instance() { 
		if (instance == null)
			instance = new LinkedSegmentFactory();
		return instance;
	}

	@Override
	public LinkedSegment createSegment() {
		return new LinkedSegment().addFirstPage();
	}

	@Override
	public Segment recreateSegmentWithId(UUID id) {
		
		LinkedSegment aSegment = new LinkedSegment(id);
		return aSegment;
	}
}
