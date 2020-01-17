package strata.segments.iface;

import java.util.UUID;

public interface Segment {

	public abstract void append(Integer value);
	public abstract Integer at(Integer index);
	public abstract Integer size();
	
	public abstract Segment splitAt(Integer index);
	public abstract void linkTo(Segment next);
	public abstract Segment next();
	public abstract Segment prev();
	
	public abstract void fileIn();
	public abstract void fileOut();
	public abstract UUID id();
}
