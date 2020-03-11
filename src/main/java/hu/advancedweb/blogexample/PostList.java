package hu.advancedweb.blogexample;

import java.util.List;

public class PostList {
    
    private long count;
    
    private long currentPageNumber;
    
    private long lastPageNumber;
    
    private List<PostShortDto> posts;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(long currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }
    
    public List<PostShortDto> getPosts() {
	return posts;
    }
    
    public void setPosts(List<PostShortDto> posts) {
	this.posts = posts;
    }

}
