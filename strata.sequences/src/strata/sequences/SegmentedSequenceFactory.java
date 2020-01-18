package strata.sequences;

import java.util.UUID;

import strata.sequences.iface.Sequence;
import strata.sequences.iface.SequenceFactory;

public class SegmentedSequenceFactory implements SequenceFactory {

	private static SequenceFactory instance;

	@Override
	public Sequence createSequence() {
		return new SegmentedSequence();
	}

	@Override
	public Sequence recreateSequenceWithId(UUID id) {
		SegmentedSequence aNewSequence = new SegmentedSequence(id);
		aNewSequence.fileIn();
		return aNewSequence;
	}

	public static SequenceFactory instance() {
		if (instance == null)
			instance = new SegmentedSequenceFactory();
		return instance;
	}

}
