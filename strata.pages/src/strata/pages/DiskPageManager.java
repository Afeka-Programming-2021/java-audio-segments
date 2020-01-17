package strata.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;

import strata.pages.iface.PageManager;

public class DiskPageManager implements PageManager {
	
	private static DiskPageManager instance;
	
	public static final int MAX_PAGES = 3;
	
	private Vector<DiskPage> pages;
	
	private Integer count;
	
	public DiskPageManager() {
		
		pages = new Vector<DiskPage>();
		count = 0;
	}
	
	public static DiskPageManager instance() {
		if (instance == null)
			instance = new DiskPageManager();
		return instance;
	}
	
	public DiskPage createPage() {
		
		DiskPage aPage = new DiskPage();
		
		try {
			aPage.id(allocateUniqueId());
			load(aPage); // adds it to the set of managed pages, will also evict pages if the set is full
			return aPage;
		}
		catch(FileNotFoundException err) {
			throw new RuntimeException("failed to create new page: " + err.getMessage());
		}
	}

	private Integer allocateUniqueId() throws FileNotFoundException {
		
		File currentFile = new File("pages/next");
		
		if (!currentFile.exists()) {
			createUniqueIdFile();
		}
		
		FileInputStream in = new FileInputStream(currentFile);
		
		Scanner scanner = new Scanner(in);
		
		Integer next = scanner.nextInt();
		
		scanner.close();
		
		next++;
		
		File newFile = new File("pages/next$");
		
		FileOutputStream out = new FileOutputStream(newFile);
		
		PrintStream printer = new PrintStream(out);
		
		printer.print(next);
		
		printer.close();
		
		currentFile.delete();
		newFile.renameTo(currentFile);
		
		return next;
		
	}
	
	private void createUniqueIdFile() {
		new File("pages").mkdirs();
		
		try (FileOutputStream out = new FileOutputStream("pages/next")) {
			PrintStream printer = new PrintStream(out);
			printer.print(0);
		}
		catch(FileNotFoundException err) {
			throw new RuntimeException("Failed to create unique id file: " + err.getMessage());
		}
		catch(IOException err) {
			throw new RuntimeException("Failed to create unique id file: " + err.getMessage());			
		}
	}

	public DiskPage recreatePageWithId(Integer pageId) throws FileNotFoundException {
		
		DiskPage aPage = new DiskPage();
		aPage.id(pageId);
		aPage.evict(); // to force fileIn to actually read the samples from disk
		aPage.fileInHeader();
		return aPage;
	}

	public void load(DiskPage aPage) throws FileNotFoundException {
		
		if (pages.size() == MAX_PAGES) {
			pages.firstElement().evict();
			pages.remove(0);
		}
		
		aPage.fileIn();
		
		aPage.ru(count);
		count++;
		
		pages.add(aPage);
	}
}
