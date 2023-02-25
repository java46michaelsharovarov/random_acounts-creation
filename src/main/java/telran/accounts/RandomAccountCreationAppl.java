package telran.accounts;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import telran.accounts.model.Account;
import telran.accounts.model.Roles;

@Slf4j
public class RandomAccountCreationAppl {

	private static int count = 1;
	private static int lengthOfPassword = 8;
	private static final String FILE_NAME = "accounts.json";

	public static void main(String[] args) { 
		
		int numberOfAccounts = 50;
		List<Account> accounts = new ArrayList<>();
		
		IntStream.range(0, numberOfAccounts).forEach(a -> accounts.add(createRandomAccount(a)));
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_NAME), accounts);
			log.info("all data is saved to file - {}", FILE_NAME);
		} catch (IOException e) {
			log.warn("data could not be saved to a file because: {}", e.getMessage());
		}
	}

	private static Account createRandomAccount(int a) {
		String username = "account" + count++; 
		String password = getRandomString(lengthOfPassword);
		String experation = LocalDateTime.now().plusHours(getRandomNumber(-100, 400)).toString();
		String[] roles = getRandomRoles();
		Account account = new Account(username, password, experation, roles);
		log.debug(account.toString());
		return account;
	}

	private static String getRandomString(int lengthOfPassword) {
		int leftLimit = 33; // '!'
	    int rightLimit = 126; // '~'
	    String generatedString = new Random().ints(leftLimit, rightLimit + 1)
	      .limit(lengthOfPassword)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
		return generatedString;
	}

	private static String[] getRandomRoles() {
		int numberOfRoles = getRandomNumber(1, Roles.values().length);
		return new Random().ints(0, Roles.values().length)
				.distinct()
			    .limit(numberOfRoles)
			    .mapToObj(e -> Roles.values()[e].name())
			    .toArray(String[]::new);	    		
	}

	private static int getRandomNumber(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

}

