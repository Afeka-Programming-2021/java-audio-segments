package strata.persistent;

import java.util.UUID;

public interface PersistentInputMedium {
	
	void open(Object key);
	void close();
	
	boolean hasNext();
	Integer nextInteger();
	UUID nextUUID();

}
