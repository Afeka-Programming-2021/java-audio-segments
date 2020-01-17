package strata.segments;

import strata.persistent.ASCIIFileInputMedium;
import strata.persistent.ASCIIFileOutputMedium;
import strata.persistent.PersistentInputMedium;
import strata.persistent.PersistentOutputMedium;

public class LinkedSegmentMedium {
	
	private static LinkedSegmentMedium instance;
	
	private PersistentInputMedium segmentInputMedium;
	private PersistentOutputMedium segmentOutputMedium;

	public LinkedSegmentMedium() {
		
		segmentInputMedium = new ASCIIFileInputMedium("segments");
		segmentOutputMedium = new ASCIIFileOutputMedium("segments");

	}
	
	public void store(LinkedSegment aLinkedSegment) {
		aLinkedSegment.storeOn(segmentOutputMedium);
	}
	
	public void load(LinkedSegment aLinkedSegment) {
		aLinkedSegment.loadFrom(segmentInputMedium);
	}

	public static LinkedSegmentMedium instance() {
		if (instance == null)
			instance = new LinkedSegmentMedium();
		return instance;
	}
}
