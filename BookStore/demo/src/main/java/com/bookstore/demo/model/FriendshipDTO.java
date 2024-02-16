package com.bookstore.demo.model;

public class FriendshipDTO {
	
    private Long id;
    private Long user1Id;
    private Long user2Id;
    
    public FriendshipDTO(Long id, Long user1Id, Long user2Id) {
    	this.id = id;
    	this.user1Id = user1Id;
    	this.user2Id = user2Id;
    }
    
    public FriendshipDTO() {
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUser1Id() {
		return user1Id;
	}
	public void setUser1Id(Long user1Id) {
		this.user1Id = user1Id;
	}
	public Long getUser2Id() {
		return user2Id;
	}
	public void setUser2Id(Long user2Id) {
		this.user2Id = user2Id;
	}
      
}
