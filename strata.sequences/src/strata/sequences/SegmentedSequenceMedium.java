package strata.sequences;

import strata.persistent.ASCIIFileInputMedium;
import strata.persistent.ASCIIFileOutputMedium;
import strata.persistent.PersistentInputMedium;
import strata.persistent.PersistentOutputMedium;

public class SegmentedSequenceMedium {
	
	private static SegmentedSequenceMedium instance;

	public static SegmentedSequenceMedium instance() {
		if (instance == null)
			instance = new SegmentedSequenceMedium();
		
		return instance;
	}
	
	private PersistentInputMedium sequenceInputMedium;
	private PersistentOutputMedium sequenceOutputMedium;

	public SegmentedSequenceMedium() {
		
		sequenceInputMedium = new ASCIIFileInputMedium("sequences");
		sequenceOutputMedium = new ASCIIFileOutputMedium("sequences");

	}
	
	public void store(SegmentedSequence aSegmentedSequence) {
		aSegmentedSequence.storeOn(sequenceOutputMedium);
	}
	
	public void load(SegmentedSequence aSegmentedSequence) {
		aSegmentedSequence.loadFrom(sequenceInputMedium);
	}	

}
