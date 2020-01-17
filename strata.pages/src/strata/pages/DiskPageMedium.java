package strata.pages;

import strata.persistent.ASCIIFileInputMedium;
import strata.persistent.ASCIIFileOutputMedium;
import strata.persistent.PersistentInputMedium;
import strata.persistent.PersistentOutputMedium;

public class DiskPageMedium {
	
	private static DiskPageMedium instance;

	public static DiskPageMedium instance() {
		if (instance == null)
			instance = new DiskPageMedium();
		return instance;
	}
	
	private PersistentInputMedium pageInputMedium;
	private PersistentOutputMedium pageOutputMedium;

	public DiskPageMedium() {
		
		pageInputMedium = new ASCIIFileInputMedium("pages");
		pageOutputMedium = new ASCIIFileOutputMedium("pages");

	}
	
	public void store(DiskPage aDiskPage) {
		aDiskPage.storeOn(pageOutputMedium);
	}
	
	public void load(DiskPage aDiskPage) {
		aDiskPage.loadFrom(pageInputMedium);
	}

	public void loadHeader(DiskPage aDiskPage) {
		aDiskPage.loadHeaderFrom(pageInputMedium);
	}
}
