package strata.persistent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.UUID;

public class ASCIIFileOutputMedium implements PersistentOutputMedium {

	PrintStream printer;
	String storageDirectoryName;
	
	public ASCIIFileOutputMedium(String aString) {

		storageDirectoryName = aString;
		
		File dir = new File(storageDirectoryName);
		
		if (!dir.exists())
			dir.mkdir();

	}
	
	
	@Override
	public void open(Object key) {
			
		try {
			printer = new PrintStream(new File(storageDirectoryName + "/" + key.toString()));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Error opening key " + key + " for write: " + e.getMessage());
		}
	}
		
	@Override
	public void close() {
		printer.close();
	}

	@Override
	public void write(Integer anInteger) {
		printer.print(anInteger.toString());
		printer.print(" ");

	}


	@Override
	public void writeUUID(UUID id) {
		printer.print(id.toString());
		printer.print(" ");
	}
}
