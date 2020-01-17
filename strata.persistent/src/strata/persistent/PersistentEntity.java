package strata.persistent;

public abstract class PersistentEntity {
	
	public abstract Object id();

	public void storeOn(PersistentOutputMedium aMedium) {
		aMedium.open(id());
		storeContentOn(aMedium);
		aMedium.close();
		storeChildren();
	}
	
	public void storeContentOn(PersistentOutputMedium aMedium) {
	}
	
	public void storeChildren() {
	}

	public void loadFrom(PersistentInputMedium aMedium) {
		aMedium.open(id());
		loadAttributesFrom(aMedium);
		loadContentFrom(aMedium);
		aMedium.close();
		loadChildren();
	}

	public void loadHeaderFrom(PersistentInputMedium aMedium) {
		aMedium.open(id());
		loadAttributesFrom(aMedium);
		aMedium.close();
	}

	public void loadAttributesFrom(PersistentInputMedium aMedium) {
	}
	
	public void loadContentFrom(PersistentInputMedium aMedium) {
	}
	
	public void loadChildren() {
	}
}
