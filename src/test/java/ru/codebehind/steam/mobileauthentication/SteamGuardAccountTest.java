package ru.codebehind.steam.mobileauthentication;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.codebehind.steam.mobileauthentication.model.Confirmation;
import ru.codebehind.steam.mobileauthentication.model.LoginRequest;
import ru.codebehind.steam.mobileauthentication.model.LoginResult;
import ru.codebehind.steam.mobileauthentication.model.LoginResultState;
import ru.codebehind.steam.mobileauthentication.model.SessionData;
import ru.codebehind.toolbelt.JsonHelper;

public class SteamGuardAccountTest {
	UserLoginService loginSvc = new UserLoginService();
	SteamGuardAccount sga;	
	SessionData session;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	private void DoLogin() throws Throwable {
		File secretLFile = new File("SecretLogin.json");
		if(secretLFile.exists() && !secretLFile.isDirectory()) { 
			LoginRequest req = JsonHelper.Deserialize(LoginRequest.class, secretLFile);
			LoginResult res = loginSvc.DoLogin(req);
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
					res = loginSvc.DoLogin(req);			
				}
				if (res.getState().equals(LoginResultState.NEED_2FA)) {
					req.setRequires2FA(true);
					req.setTwoFactorCode(sga.GenerateSteamGuardCode());
					res = loginSvc.DoLogin(req);
				}
			}
			if (!res.getState().equals(LoginResultState.LOGIN_OK))
				throw new Exception("Error during login!");
			session = res.getSession();
		}
	}

	@Before
	public void setUp() throws Throwable {
		// load secret settings
		File secretSGFile = new File("SecretSteamGuard.json");
		if(secretSGFile.exists() && !secretSGFile.isDirectory()) {
			sga = new SteamGuardAccount(JsonHelper.Deserialize(SteamGuardAccount.Config.class, secretSGFile));
		}
		// login to account for cookies
		DoLogin();
		// set them to SteamGuardAccount
		sga.setSession(session);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFetchConfirmations() throws Throwable {
		Confirmation[] confirmations = sga.FetchConfirmations();
		assertNotNull(confirmations);
	}
	@Test
	public void testAcceptConfirmation() throws Throwable {
		Confirmation[] confirmations = sga.FetchConfirmations();
		assertNotNull(confirmations);
		assertTrue(confirmations.length > 0);
		// if many confirmations - one is enough
		boolean denyRes = sga.AcceptConfirmation(confirmations[0]);
		assertEquals(true, denyRes);
	}

	@Test
	public void testDenyConfirmation() throws Throwable {
		Confirmation[] confirmations = sga.FetchConfirmations();
		assertNotNull(confirmations);
		assertTrue(confirmations.length > 0);
		// if many confirmations - one is enough
		boolean denyRes = sga.DenyConfirmation(confirmations[0]);
		assertEquals(true, denyRes);
	}

	@Test
	public void testRefreshSession() {
		fail("Not yet implemented");
	}

	@Test
	public void testRefreshSessionAsync() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateConfirmationURL() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateConfirmationURLString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateConfirmationQueryParams() {
		fail("Not yet implemented");
	}

}
