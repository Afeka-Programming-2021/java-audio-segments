package strata.pages;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

class TestDiskPage {

	@Test
	void testCreate() {
		DiskPage aPage = DiskPageManager.instance().createPage();
		
		for(int i = 0; i < DiskPage.PAGE_SIZE; i++) {
			aPage.append(i);
		}
		
		assertEquals(aPage.size(), DiskPage.PAGE_SIZE);
		
		for(int i = 0; i < DiskPage.PAGE_SIZE; i++) {
			assertEquals(aPage.at(i), i);
		}
	}
	@Test
	void testPageIO() throws FileNotFoundException {

		DiskPage aPage = DiskPageManager.instance().createPage();

		for(int i = 0; i < 2; i++) {
			aPage.append(i);
		}
		
		aPage.fileOut();
		aPage.fileIn();

		for(int i = 2; i < DiskPage.PAGE_SIZE; i++) {
			aPage.append(i);
		}

		aPage.fileOut();

		assertEquals(aPage.size(), DiskPage.PAGE_SIZE);
		
		for(int i = 0; i < DiskPage.PAGE_SIZE; i++) {
			assertEquals(aPage.at(i), i);
		}
	}
	
	@Test
	void testLoad() throws FileNotFoundException {
		
		DiskPage aPage = DiskPageManager.instance().recreatePageWithId(2); 
				
		assertEquals(DiskPage.PAGE_SIZE, aPage.size());
		
		for(int i = 0; i < DiskPage.PAGE_SIZE; i++) {
			assertEquals(aPage.at(i), i);
		}
	}
	
	@Test
	void testEviction() {

		int i = 0;
		
		for(int p = 0; p < 5; p++) {
		
			DiskPage aPage = DiskPageManager.instance().createPage();

			for(int j = 0; j < DiskPage.PAGE_SIZE; j++) {
				aPage.append(i);
				i++;
			}
		}
		
		assertTrue(true);
	}
}
