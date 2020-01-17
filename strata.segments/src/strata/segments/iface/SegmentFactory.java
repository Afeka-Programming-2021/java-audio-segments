package strata.segments.iface;

import java.util.UUID;

import strata.segments.LinkedSegmentFactory;

public interface SegmentFactory {

	public static SegmentFactory instance() { return LinkedSegmentFactory.instance(); }
	
	public Segment createSegment();

	public Segment recreateSegmentWithId(UUID id);
	
}
