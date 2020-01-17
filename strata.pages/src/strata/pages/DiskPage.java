package strata.pages;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import strata.pages.iface.Page;
import strata.persistent.PersistentInputMedium;
import strata.persistent.PersistentOutputMedium;
import strata.persistent.PersistentEntity;
import strata.persistent.PersistentEntityBuilder;

public class DiskPage extends PersistentEntity implements Page { 
	
	private Integer id;
	private Vector<Integer> samples;
	private Integer size;
	private Integer ru;
	private boolean dirty;
	private boolean evicted;

	public DiskPage() {
		samples = new Vector<Integer>();
		dirty = false;
		evicted = false;
		size = 0;
	}
	
	public void id(Integer anInteger) {
		id = anInteger;
	}
	
	public void evict() {
		if (dirty) {
			fileOut();
		}
		samples.removeAllElements();
		evicted = true;
	}
	
	public void ru(Integer anInteger) {
		ru = anInteger;
	}
	
	@Override
	public void append(Integer value) {
	
		reload();
				
		if (evicted)
			throw new RuntimeException("Attempt to append to an evicted page");
		
		if (isFull())
			throw new RuntimeException("Attempt to append to a full page");
		
		dirty = true;
		
		samples.add(value);
		
		size = samples.size();
	}

	@Override
	public Integer at(Integer index) {
		
		reload();
		
		if (evicted)
			throw new RuntimeException("Attempt to read from an evicted page");
		
		return samples.elementAt(index);
	}

	private void reload() {
		if (evicted) {
			try {
				DiskPageManager.instance().load(this);
			}
			catch(FileNotFoundException err) {
				throw new RuntimeException("page file not found while calling DiskPage.at");
			}
		}
	}
	@Override
	public boolean isFull() {
		return size() == PAGE_SIZE;
	}

	@Override
	public Integer size() {	
		return size;
	}

	@Override
	public DiskPage[] splitAt(Integer offset) {
		
		assert offset > 0;
		assert offset < size;

		DiskPage ps[] = new DiskPage[2];
		
		ps[0] = DiskPageManager.instance().createPage();
		ps[1] = DiskPageManager.instance().createPage();
		
		int i = 0;
		for (; i < offset; i++) {
			ps[0].append(at(i));
		}
		
		for(; i < samples.size(); i++) {
			ps[1].append(at(i));
		}
		
		return ps;
	}

	@Override
	public Integer id() {
		return id;
	}
	
	public void fileInHeader() {

		if (!evicted)
			return; // already in memory
		
		if (dirty)
			throw new RuntimeException("Attempt to file in a dirty page");

		DiskPageMedium.instance().loadHeader(this);
		
	}
	
	@Override
	public void fileIn() {

		if (!evicted)
			return; // already in memory
		
		if (dirty)
			throw new RuntimeException("Attempt to file in a dirty page");
		
		DiskPageMedium.instance().load(this);
		
		evicted = false;
	}

	@Override
	public void fileOut() {

		if (!dirty) return;
		
		if (evicted) 
			throw new RuntimeException("Attempt to file out an evicted page");
		
		DiskPageMedium.instance().store(this);
		
		dirty = false;
	}


	@Override
	public void storeContentOn(PersistentOutputMedium aMedium) {
		
		aMedium.write(samples.size());
		
		for(int i = 0 ; i < samples.size(); i++) {
			aMedium.write(samples.elementAt(i));
		}
	}
	
	@Override
	public void loadAttributesFrom(PersistentInputMedium aMedium) {
		
		size = aMedium.nextInteger();

	}
	
	@Override
	public void loadContentFrom(PersistentInputMedium aMedium) {
		
		for(int i = 0 ; i < size; i++) {
			samples.add(aMedium.nextInteger());
		}
	}
	
}
