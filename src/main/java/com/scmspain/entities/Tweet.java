package com.scmspain.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tweet {
	
	public static final int MAX_TWEET_STORAGE_LENGTH = 540;
	
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String publisher;
    @Column(nullable = false, length = MAX_TWEET_STORAGE_LENGTH)
    private String tweet;
    @Column (nullable=true)
    private Long pre2015MigrationStatus = 0L;
    
    @Column
    private LocalDateTime discardDate;
    
    public Tweet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Long getPre2015MigrationStatus() {
        return pre2015MigrationStatus;
    }

    public void setPre2015MigrationStatus(Long pre2015MigrationStatus) {
        this.pre2015MigrationStatus = pre2015MigrationStatus;
    }

	/**
	 * @return the discardDate
	 */
	public LocalDateTime getDiscardDate() {
		return discardDate;
	}

	/**
	 * @param discardDate the discardDate to set
	 */
	public void setDiscardDate(LocalDateTime discardDate) {
		this.discardDate = discardDate;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tweet other = (Tweet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tweet [id=" + id + ", publisher=" + publisher + ", tweet="
				+ tweet + ", pre2015MigrationStatus=" + pre2015MigrationStatus
				+ ", discardDate=" + discardDate + "]";
	}

}
