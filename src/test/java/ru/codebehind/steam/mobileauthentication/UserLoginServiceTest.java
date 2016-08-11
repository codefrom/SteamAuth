/**
 * 
 */
package ru.codebehind.steam.mobileauthentication;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.codebehind.steam.mobileauthentication.model.LoginRequest;
import ru.codebehind.steam.mobileauthentication.model.LoginResult;
import ru.codebehind.steam.mobileauthentication.model.LoginResultState;

/**
 * @author irateev
 *
 */
public class UserLoginServiceTest extends UserLoginService {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ru.codebehind.steam.mobileauthentication.UserLoginService#DoLogin(ru.codebehind.steam.mobileauthentication.model.LoginRequest)}.
	 * @throws Throwable 
	 */
	@Test
	public final void testDoLogin_BAD_CREDENTIALS() throws Throwable {
		LoginRequest req = new LoginRequest();
		req.setUsername("test");
		req.setPassword("test");
		LoginResult res = this.DoLogin(req);
		if (res.getState().equals(LoginResultState.NEED_CAPTCHA)) {
			System.out.println("Enter CAPTCHA from:");
			System.out.println("https://steamcommunity.com/public/captcha.php?gid=" + res.getCaptchaGID());
			
			Scanner scanner = new Scanner(System.in);
			req.setRequiresCaptcha(true);
			req.setCaptchaGID(res.getCaptchaGID());
			req.setCaptchaText(scanner.nextLine());
			scanner.close();
			res = this.DoLogin(req);			
		}
		assertEquals(res.getState(), LoginResultState.BAD_CREDENTIALS);
	}
	
	@Test
	public final void testDoLogin_NEED_CAPTCHA() throws Throwable {
		LoginRequest req = new LoginRequest();
		req.setUsername("test");
		req.setPassword("test");
		LoginResult res = this.DoLogin(req);
		assertTrue(res.getState().equals(LoginResultState.BAD_CREDENTIALS) ||
				   res.getState().equals(LoginResultState.NEED_CAPTCHA));
	}

	@Test
	public final void testDoLogin_OK() throws Throwable {
		LoginRequest req = new LoginRequest();
		req.setUsername("xxx");
		req.setPassword("xxx");
		LoginResult res = this.DoLogin(req);
		while (res.getState().equals(LoginResultState.NEED_CAPTCHA)) {
			System.out.println("Enter CAPTCHA from:");
			System.out.println("https://steamcommunity.com/public/captcha.php?gid=" + res.getCaptchaGID());
			
			Scanner scanner = new Scanner(System.in);
			req.setRequiresCaptcha(true);
			req.setCaptchaGID(res.getCaptchaGID());
			req.setCaptchaText(scanner.nextLine());
			scanner.close();
			res = this.DoLogin(req);			
		}
		assertEquals(res.getState(), LoginResultState.LOGIN_OK);
	}
}
