package telran.accounts;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import telran.accounts.model.Account;
import telran.accounts.model.Roles;

@Slf4j
public class RandomAccountCreationAppl {

	private static final int MAX_HOURS = 400;
	private static final int MIN_HOURS = -100;
	private static final int NUMBER_OF_ACCOUNTS = 50;
	private static final String BASE_USER_NAME = "account";
	private static final int LENGTH_OF_PASSWORD = 8;
	private static final String FILE_NAME = "accounts.json";
	private static ThreadLocalRandom threadRandom = ThreadLocalRandom.current();

	public static void main(String[] args) { 
		
		List<Account> accounts = IntStream.rangeClosed(1, NUMBER_OF_ACCOUNTS)
		.mapToObj(RandomAccountCreationAppl::createRandomAccount)
		.toList();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_NAME), accounts);
			log.info("all data is saved to file - {}", FILE_NAME);
		} catch (IOException e) {
			log.warn("data could not be saved to a file because: {}", e.getMessage());
		}
	}

	private static Account createRandomAccount(int accountNumber) {
		String username = BASE_USER_NAME + accountNumber; 
		String password = getRandomString(LENGTH_OF_PASSWORD);
		String expiration = getRandomExpiration(MIN_HOURS, MAX_HOURS);
		String[] roles = getRandomRoles();
		boolean revoked = getRandomNumber(0, 1) > 0;
		Account account = new Account(username, password, expiration, roles, revoked);
		log.debug(account.toString());
		return account;
	}

	private static String getRandomExpiration(int min, int max) {
		return LocalDateTime.now().plusHours(getRandomNumber(min, max)).toString();
	}

	private static String getRandomString(int lengthOfPassword) {
		return threadRandom.ints(lengthOfPassword, '!', '~' + 1)
			      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			      .toString();
//Solution by Granovsky
//		return threadRandom.ints(lengthOfPassword, '!', '~' + 1).mapToObj(code -> (char)code + "")
//				.collect(Collectors.joining());
	}

	private static String[] getRandomRoles() {
		int numberOfRoles = getRandomNumber(1, Roles.values().length);
		return threadRandom.ints(numberOfRoles, 0, Roles.values().length)
				.distinct()
			    .mapToObj(e -> Roles.values()[e].name())
			    .toArray(String[]::new);	    		
	}

	private static int getRandomNumber(int min, int max) {
		return threadRandom.nextInt(min, max + 1);
	}

}

