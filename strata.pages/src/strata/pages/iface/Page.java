package strata.pages.iface;

public interface Page {

	public static final int PAGE_SIZE = 3;
	
	public void append(Integer value);
	public Integer at(Integer index);
	public void fileIn();
	public void fileOut();
	public boolean isFull();
	public Integer size();
	public Page[] splitAt(Integer index);
	public Integer id();
}
