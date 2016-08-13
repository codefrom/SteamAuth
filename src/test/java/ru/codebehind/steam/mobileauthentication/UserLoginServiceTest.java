/**
 * 
 */
package ru.codebehind.steam.mobileauthentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
		assertEquals(LoginResultState.BAD_CREDENTIALS, res.getState());
	}
	
	@Test
	public final void testDoLogin_NEED_CAPTCHA_OR_BAD_CREDS() throws Throwable {
		LoginRequest req = new LoginRequest();
		req.setUsername("test");
		req.setPassword("test");
		LoginResult res = this.DoLogin(req);
		assertTrue(res.getState().equals(LoginResultState.BAD_CREDENTIALS) ||
				   res.getState().equals(LoginResultState.NEED_CAPTCHA));
		res = this.DoLogin(req);
		assertTrue(res.getState().equals(LoginResultState.NEED_CAPTCHA));
	}

	@Test
	public final void testDoLogin_OK() throws Throwable {
		LoginRequest req = new LoginRequest();
		// TODO : change it to valid user name password for testing
		req.setUsername("xxx");
		req.setPassword("xxx");
		LoginResult res = this.DoLogin(req);
		while (res.getState().equals(LoginResultState.NEED_CAPTCHA) ||
			   res.getState().equals(LoginResultState.NEED_2FA)) {
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
			if (res.getState().equals(LoginResultState.NEED_2FA)) {
				System.out.println("Enter mobile auth code:");
				
				Scanner scanner = new Scanner(System.in);
				req.setRequires2FA(true);
				req.setTwoFactorCode(scanner.nextLine());
				scanner.close();
				res = this.DoLogin(req);
			}
		}
		assertEquals(LoginResultState.LOGIN_OK, res.getState());
	}
}
