package validator;

public class SimilarityFilter {
	private boolean isExact;
	private boolean isPlugin;
	private boolean isSubsumes;
	private boolean isSibling;
	private boolean isFail;
	
	private String testedBy;
	
	public SimilarityFilter(boolean isExact, boolean isPlugin, boolean isSubsumes, boolean isSibling, boolean isFail)
	{
		this.isExact = isExact;
		this.isPlugin = isPlugin;
		this.isSubsumes = isSubsumes;
		this.isSibling = isSibling;
		this.isFail = isFail;
		testedBy = new String();
	}
	
	public boolean isExact() {
		return isExact;
	}
	public void setExact(boolean isExact) {
		this.isExact = isExact;
	}
	public boolean isPlugin() {
		return isPlugin;
	}
	public void setPlugin(boolean isPlugin) {
		this.isPlugin = isPlugin;
	}
	public boolean isSubsumes() {
		return isSubsumes;
	}
	public void setSubsumes(boolean isSubsumes) {
		this.isSubsumes = isSubsumes;
	}
	public boolean isSibling() {
		return isSibling;
	}
	public void setSibling(boolean isSibling) {
		this.isSibling = isSibling;
	}
	public boolean isFail() {
		return isFail;
	}
	public void setFail(boolean isFail) {
		this.isFail = isFail;
	}
	
	public boolean isAcceptable(int simDeg)
	{
		switch(simDeg)
		{
		case FunctionalMatcher.EXACT:
			setTestedBy("EXACT");
			return isExact();
		case FunctionalMatcher.PLUGIN:
			setTestedBy("PLUGIN");			
			return isPlugin();
		case FunctionalMatcher.SUBSUMES:
			setTestedBy("SUBSUMES");			
			return isSubsumes();
		case FunctionalMatcher.SIBLING:
			setTestedBy("SIBLING");
			return isSibling();
		case FunctionalMatcher.FAIL:
			setTestedBy("FAIL");
			return isFail();
		}
		return true;
	}

	private void setTestedBy(String testedBy) {
		this.testedBy = testedBy;
	}

	public String getTestedBy() {
		return testedBy;
	}
}
