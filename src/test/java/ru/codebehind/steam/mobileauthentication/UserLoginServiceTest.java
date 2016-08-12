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
		assertEquals(LoginResultState.BAD_CREDENTIALS, res.getState());
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
		assertEquals(LoginResultState.LOGIN_OK, res.getState());
	}
	
	@Test
	public final void testSecurePassword() throws Throwable {
		String expected = "b/ZN8HvytJpOD2t3M8Sg8RBP/dRHDr1QwgbqDHjAKW6EgZ8WuwRTHa4H4oUKU0BLPvAk/tItTCv0ILrREDULVaWpWQCfYfl+x9Y/jJijn2DJaJbSFdMSri2PMhVWnAVICcNEoeTuj3U5hMDudUc7Rlg2iTWI2guxELqp2ZHiU3HsYvQzCgVbPCKM20bz2fA1e5Ub4AiPB4H3Oqs7JFEtkCKomJ0im0dcRKGJt39ybjp5lQFsgM4J4V35x9EOxh1bChg+RBRuPz9RPHNlOvziWnKJcY0NHmsLk0Jz32ZEoEDEvyiQLL2oDHpHpznYBV3hjyq6yaTo9PU6pAhRGRMzFQ==";
        String password = "test";
        String exponent = "010001";
        //String modulus = "AF11B75E2714AEBC3E0D308E80B82C447E9163FD9974BAAD28BEC84E62C61FB65AC852C226DDF087D6DFFFF23089557EA2E8C7C4ADF68A4C615871652846EA9937FD25828DD6A732FEF9FA6E5290931FB0EA75FCCCEB10985772492106C468E23A43780B6886934FC7F6F91182E6A2BDBC6257B18F9F46A066C9E928E0CCCB027654E84ABBE2441FC3B8850AA6CAE9EEF5549ADB0F7C5F2036995E9EB938E5ED61807E43BCA6AAAE985F669383490FB4620A672DC24CF758996BA8E9CC236469E386B6E4ABA431B13FD71E2B499E93BFC7D7E687298404A0CB8D50171BF3D7DDE6EF4E7E26866F911CDB6A76B7E743F2DE25200234C12F6BFDA51D98B677BCFD";
        String modulus = "9c5dc3ff5623d05ba1c1c3ffe821611ae92d5a109aef865ce39c86a8258415636293efda015b493309e97fe0c26d25b283219e37b5418bf03d662eb87c48049a1a5823a325adfd4b52a1a4e0c35779008d32140263bffe6037a5b44626d044bc4bcb2ef4d57652f4e6c9cd710293b76725a11c0c9f211f7570516125181558cd";
        String actual = getSecurePassword(password, modulus, exponent);
		assertEquals(expected, actual);
	}
}
