package strata.sequences.iface;

import java.util.UUID;

import strata.sequences.SegmentedSequenceFactory;

public interface SequenceFactory {
	
	public Sequence createSequenece();
	public Sequence recreateSequenceWithId(UUID id);
}
