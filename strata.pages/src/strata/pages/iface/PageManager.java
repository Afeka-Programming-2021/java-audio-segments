package strata.pages.iface;

import java.io.FileNotFoundException;

import strata.pages.DiskPage;
import strata.pages.DiskPageManager;

public interface PageManager {
	
	public static PageManager instance() { return DiskPageManager.instance(); }

	public Page createPage();
	
	public Page recreatePageWithId(Integer pageId) throws FileNotFoundException;

}
