package hu.advancedweb.blogexample;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.thymeleaf.expression.Sets;

@Entity
@Table(name = "POST")
public class Post {

    @Id
    private Long id;

    @Column
    private String title;

    @Column
    private String content;
    
    @Column
    private String status;

    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public String getStatus() {
	return status;
    }
    
    public void setStatus(String status) {
	this.status = status;
    }

}