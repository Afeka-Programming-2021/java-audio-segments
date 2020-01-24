package strata.sequences.iface;

import java.util.UUID;

import strata.sequences.SegmentedSequenceFactory;

public interface SequenceFactory {
	public Sequence createSequence();
	public Sequence recreateSequenceWithId(UUID id);

	public static SegmentedSequenceFactory createFactory() {
		return (SegmentedSequenceFactory) SegmentedSequenceFactory.instance();
	}
}
