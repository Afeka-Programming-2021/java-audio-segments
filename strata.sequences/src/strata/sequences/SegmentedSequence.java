package strata.sequences;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import strata.persistent.PersistentEntity;
import strata.persistent.PersistentInputMedium;
import strata.persistent.PersistentOutputMedium;
import strata.segments.iface.Segment;
import strata.segments.iface.SegmentFactory;
import strata.sequences.iface.Sequence;
import strata.sequences.iface.SequenceFactory;

public class SegmentedSequence extends PersistentEntity implements Sequence {
	
	UUID id;
	
	private Segment head;
	private Segment tail;
	
	public SegmentedSequence() {
		
		id = UUID.randomUUID();
		
		head = SegmentFactory.instance().createSegment();
		tail = SegmentFactory.instance().createSegment();
		
		reset();
	}
	
	public SegmentedSequence(UUID aUUID) {
		id = aUUID;
		head = SegmentFactory.instance().createSegment();
		tail = SegmentFactory.instance().createSegment();
		// no need to link them as this command must always be immediately followed by a call to fileIn
	}

	private void reset() {
		Segment p = SegmentFactory.instance().createSegment();
		head.linkTo(p);
		p.linkTo(tail);
	}

	@Override
	public void append(Integer value) {

		tail.prev().append(value);

	}

	@Override
	public Integer at(Integer index) {
		Map.Entry<Segment, Integer> entry = segmentOffsetAt(index);
		return entry.getKey().at(entry.getValue());
	}
	
	private Map.Entry<Segment, Integer> segmentOffsetAt(Integer index) {
		
		Integer i = index;
		Segment p = head.next();
		while (p != tail && i >= p.size())
		{
			i = i - p.size();
			p = p.next();
		}
		
		if (p == tail)
			throw new RuntimeException("index out of bounds");
		
		return new AbstractMap.SimpleEntry<Segment, Integer>(p, i);
	}

	@Override
	public void cut(Integer from, Integer to) {
		Segment b = splitAt(from);
		Segment e = splitAt(to);
		b.linkTo(e.next());
	}

	private Segment splitAt(Integer index) {
		
		Map.Entry<Segment, Integer> entry = segmentOffsetAt(index);
		
		return entry.getKey().splitAt(entry.getValue());
	}

	@Override
	public void paste(Sequence aSequence, Integer index) {
		
		Segment aSegment = splitAt(index);
		
		((SegmentedSequence)aSequence).pasteAfter(aSegment);

	}
	
	private void pasteAfter(Segment aSegment) {
		tail.prev().linkTo(aSegment.next());
		aSegment.linkTo(head.next());
		reset(); // clear this sequence as its content has been pasted into another sequence.
	}

	@Override
	public Integer size() {
		Integer n = 0;
		Segment p = head.next();
		while (p != tail) {
			n += p.size();
			p = p.next();
		}
		return n;
	}

	@Override
	public UUID id() {
		return id;
	}

	@Override
	public void loadContentFrom(PersistentInputMedium aMedium) {
				
		head.linkTo(tail);
		
		while(aMedium.hasNext()) {
			Segment aNewSegment = SegmentFactory.instance().recreateSegmentWithId(aMedium.nextUUID());
			tail.prev().linkTo(aNewSegment);
			aNewSegment.linkTo(tail);
		}
	}

	@Override
	public void loadChildren() {
		Segment p = head.next();
		while (p != tail) {
			p.fileIn();
			p = p.next();
		}
	}

	@Override
	public void fileIn() {
		SegmentedSequenceMedium.instance().load(this);
	}

	@Override
	public void storeContentOn(PersistentOutputMedium aMedium) {
		Segment p = head.next();
		while (p != tail) {
			aMedium.writeUUID(p.id());
			p = p.next();
		}
	}
	
	@Override 
	public void storeChildren() {
		Segment p = head.next();
		while (p != tail) {
			p.fileOut();
			p = p.next();
		}	
	}
	
	@Override
	public void fileOut() {
		SegmentedSequenceMedium.instance().store(this);		
	}

	@Override
	public Sequence duplicate() {
		fileOut();
		return SegmentedSequenceFactory.instance().recreateSequenceWithId(id());
	}
}
