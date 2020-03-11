package hu.advancedweb.blogexample;

import java.util.List;

public class PostShortDto {
    
    private Long id;

    private String title;
    
    private String status;

    private List<String> tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getStatus() {
	return status;
    }
    
    public void setStatus(String status) {
	this.status = status;
    }
    
}
