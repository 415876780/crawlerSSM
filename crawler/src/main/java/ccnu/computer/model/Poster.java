package ccnu.computer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_poster")
public class Poster {
	private int id;
	private String topic;
	private String user;
	private String content;
	private String agreement;
	private String transmit;
	private String time;
	private String comment;
	private String isRelated;
	private String isLabel;
	private String title;
	//是由谁标记的
	private String labelname;
	
	public String getLabelname() {
		return labelname;
	}
	public void setLabelname(String labelname) {
		this.labelname = labelname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsRelated() {
		return isRelated;
	}
	public void setIsRelated(String isRelated) {
		this.isRelated = isRelated;
	}
	public String getIsLabel() {
		return isLabel;
	}
	public void setIsLabel(String isLabel) {
		this.isLabel = isLabel;
	}
	@GeneratedValue
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAgreement() {
		return agreement;
	}
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}
	public String getTransmit() {
		return transmit;
	}
	public void setTransmit(String transmit) {
		this.transmit = transmit;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
