package strata.persistent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.UUID;

public class ASCIIFileInputMedium implements PersistentInputMedium {

	private Scanner scanner;
	String storageDirectoryName;
	
	public ASCIIFileInputMedium(String aString) {
		storageDirectoryName = aString;
	}
	
	@Override
	public void open(Object key) {
		
		FileInputStream in;
		try {
			in = new FileInputStream(storageDirectoryName + "/" + key.toString());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Error opening key: " + key.toString() + ": " + e.getMessage());
		}
		
		scanner = new Scanner(in);
	}

	@Override
	public void close() {
		
		scanner.close();

	}

	@Override
	public boolean hasNext() {

		return scanner.hasNext();
	}

	@Override
	public Integer nextInteger() {

		return scanner.nextInt();
	}

	@Override
	public UUID nextUUID() {
		return UUID.fromString(scanner.next());
	}

}
