package strata.sequences.iface;

import java.util.UUID;

public interface Sequence {
	public Integer size();
	public Integer at(Integer index);
	public UUID id();
	public void append(Integer value);
	public void cut(Integer from, Integer to);
	public void paste(Sequence aSequence, Integer after);
	public Sequence duplicate();
	public void fileIn();
	public void fileOut();
}
