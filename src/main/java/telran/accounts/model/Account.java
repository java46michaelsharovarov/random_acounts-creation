package telran.accounts.model;

import lombok.Value;

@Value
public class Account {

	private String username;
	private String password;	
	private String experation;
	private String[] roles;
	
}
