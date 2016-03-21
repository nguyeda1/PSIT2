package ch.avocado.share.service;

import ch.avocado.share.model.data.*;
import ch.avocado.share.service.Impl.MailingService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by bergm on 21/03/2016.
 */
public class MailingServiceTest {

    private MailingService service;

    @Before
    public void init()
    {
        service = new MailingService();
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_verificationEmail_noParameter() {
        service.sendRequestAccessEmail(null, null, "");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_requestEmail_noParameter() {
        service.sendVerificationEmail(null);
    }

    @Test
    public void test_requestSendMail()
    {
        User user = new User("123", new ArrayList<Category>(), new Date(01, 01, 1999), null, "", "", new UserPassword("123456"), "", "", "", new EmailAddress(true, "bergmsas@students.zhaw.ch", new EmailAddressVerification( new Date(01, 01, 1999), "123456" )));
        File file = new File("123", new ArrayList<Category>(), new Date(01, 01, 1999), null, "", "", "", "", "",  new Date(01, 01, 1999), "", "");

        assertTrue(service.sendRequestAccessEmail(user, file, "MESSAGE"));
    }

    @Test
    public void test_verificationSendMail()
    {
        User user = new User("123", new ArrayList<Category>(), new Date(01, 01, 1999), null, "", "", new UserPassword("123456"), "", "", "", new EmailAddress(true, "bergmsas@students.zhaw.ch", new EmailAddressVerification( new Date(01, 01, 1999), "123456" )));

        assertTrue(service.sendVerificationEmail(user));
    }

}

