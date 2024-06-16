package edu.ap.softwareproject.api.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.jupiter.api.Test;

import edu.ap.softwareproject.MailObjectMother;

class MailUtilTest {

  @Test
  void givenMimeMessage_shouldBeTheSame() throws UnsupportedEncodingException, MessagingException {

    assertThat(MailObjectMother.mailRegexCheck(MailObjectMother.mailDetails().get("receiver"))).isTrue();

    final String sender = MailObjectMother.sender();
    final String password = MailObjectMother.password();

    Session session = MailObjectMother.session(sender, password);

    Map<String, String> mail = MailObjectMother.mailDetails();

    Message msg = MailObjectMother.createMimeMessage(session, sender, mail.get("senderName"), mail.get("receiver"),
        mail.get("subject"), mail.get("htmlBody"));

    assertThat(mail.get("receiver")).contains(msg.getRecipients(RecipientType.TO)[0].toString());
  }
}