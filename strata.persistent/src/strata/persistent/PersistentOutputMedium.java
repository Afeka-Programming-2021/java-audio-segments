package strata.persistent;

import java.util.UUID;

public interface PersistentOutputMedium {
		 
	void open(Object key);
	void close();
	
	void write(Integer anInteger);
	void writeUUID(UUID id);

}
