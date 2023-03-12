package telran.accounts.model;

import lombok.Value;

@Value
public class Account {

	private String username;
	private String password;	
	private String expiration;
	private String[] roles;
	private boolean revoked;
	
}
