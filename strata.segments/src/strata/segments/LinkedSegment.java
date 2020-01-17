package strata.segments;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import strata.pages.iface.Page;
import strata.pages.iface.PageManager;
import strata.persistent.PersistentInputMedium;
import strata.persistent.PersistentOutputMedium;
import strata.persistent.PersistentEntity;
import strata.segments.iface.Segment;

public class LinkedSegment extends PersistentEntity implements Segment {

	private UUID id;
	private Vector<Page> pages;
	private LinkedSegment next;
	private LinkedSegment prev;
	
	public LinkedSegment() {
		pages = new Vector<Page>();
		id = UUID.randomUUID();
	}

	public LinkedSegment(UUID aUUID) {
		pages = new Vector<Page>();
		id = aUUID;
	}
	
	@Override
	public void append(Integer value) {
		
		if (pages.lastElement().isFull())
			pages.add(PageManager.instance().createPage());
		
		pages.lastElement().append(value);

	}

	@Override
	public Integer at(Integer index) {
		return pages.elementAt( index / Page.PAGE_SIZE).at(index % Page.PAGE_SIZE);
	}

	@Override
	public Integer size() {
		return (pages.size() - 1) * Page.PAGE_SIZE + pages.lastElement().size(); 
	}

	@Override
	public LinkedSegment splitAt(Integer anInteger) {
		
		assert anInteger >= 0;
		assert anInteger <= size();
		
		if (anInteger == 0)
			return this;
		
		if (anInteger == size())
			return this;
		
		Integer index = anInteger / Page.PAGE_SIZE;
		
		Integer offset = anInteger % Page.PAGE_SIZE;
		
		LinkedSegment left = new LinkedSegment();

		LinkedSegment mid = new LinkedSegment();
		
		LinkedSegment right = new LinkedSegment();
		
		for(int i = 0 ; i < index ; i++) {
			left.pages.add(pages.elementAt(i));
		}
		
		if (offset == 0) {
			right.pages.add(pages.elementAt(index));
		}
		else {
			Page[] ps = pages.elementAt(index).splitAt(offset);
			left.pages.add(ps[0]);
			mid.pages.add(ps[1]);
		}	
		
		for(int i = index + 1; i < pages.size(); i++) {
			right.pages.add(pages.elementAt(i));
		}
		
		prev.linkTo(left);
		
		if (offset == 0) {
			left.linkTo(right);
		} else {
			left.linkTo(mid);
			mid.linkTo(right);
		}
		
		if (offset > 0 && index == pages.size() - 1) {
			mid.linkTo(next);
		} else {
			right.linkTo(next);
		}
		
		return left;
	}

	public void linkTo(Segment aSegment) {
		next = (LinkedSegment)aSegment;
		next.prev = this;
	}

	public UUID id() {
		return id;
	}

	public Segment next() {
		return next;
	}

	public Segment prev() {
		return prev;
	}

	public LinkedSegment addFirstPage() {
		assert pages.isEmpty();
		
		pages.add(PageManager.instance().createPage());
		return this;
	}

	// file in/out
	
	@Override
	public void fileIn() {
		
		LinkedSegmentMedium.instance().load(this);
	}
	
	public void loadContentFrom(PersistentInputMedium aMedium) {
		
		try {
			pages.clear();
		
			while(aMedium.hasNext()) {
				pages.add(PageManager.instance().recreatePageWithId(aMedium.nextInteger()));
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in loadContentFrom: " + e.getMessage());
		}
	}
	
	@Override
	public void fileOut() {
		
		LinkedSegmentMedium.instance().store(this);
	}
	
	public void storeContentOn(PersistentOutputMedium aMedium) {
		for(Page page : pages) {
			aMedium.write(page.id());
		}
	}
	
	public void storeChildren() {
		for(Page page : pages) {
			page.fileOut();
		}
	}
}
