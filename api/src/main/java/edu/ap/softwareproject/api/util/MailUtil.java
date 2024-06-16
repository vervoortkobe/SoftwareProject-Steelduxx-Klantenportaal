package edu.ap.softwareproject.api.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Pattern;

import static edu.ap.softwareproject.api.ApiApplication.sentry;

/**
 * This class provides utility methods for sending emails in the application.
 * It includes methods for sending emails related to registered accounts,
 * password set, and order status updates.
 */
@Component
public final class MailUtil {

  // Declare the sender's email address and password
  private static String sender = System.getenv("MAIL_ADDRESS");
  private static String password = System.getenv("MAIL_PWD");

  // #region Registered Account
  /**
   * Sends an email to the registered account with account details.
   *
   * @param receiver     the email address of the receiver
   * @param accountName  the name associated with the account
   * @param dashboardURL the URL to access the account's dashboard
   */
  public static void sendMailRegisteredAccount(String receiver, String accountName,
      String dashboardURL) {

    String content = ""
        .concat("""
                    <!DOCTYPE html>
                    <html lang="en">

                    <head>
                      <meta charset="UTF-8">
                      <meta http-equiv="X-UA-Compatible" content="IE=edge">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>Steelduxx &bull; New account</title>
                    </head>

                    <body>
                      <center>
                        <header>
                          <section class="hero is-primary">
                            <article class="hero-body">
                              <article class="container">
                                <h1 class="title">
                                  Welcome to Steelduxx!
                                </h1>
                                <h2 class="subtitle">
                                  Thank you for registering a new account with us.
                                </h2>
                              </article>
                            </article>
                          </section>
                        </header>

                        <main>
                          <section class="outer">
                            <section class="inner box">
            """)
        .concat(accountName)
        .concat(
            """
                        ,
                        <br>
                        <br>
                        <p>
                          Your account has been approved.<br>
                          You can now log in to your account and start using our services.
                        </p>
                    <article class="grid box">
                      <div class="details">
                        <p class="p">
                          Here are your account details:
                        </p>
                        <table>
                <tr>
                  <th style="text-align: left;">- <strong>Email: </strong></th>
                  <td>""")
        .concat(receiver)
        .concat("""
                </td>
            </tr>
            <tr>
              <th style="text-align: left;">- <strong>Login here: </strong></th>
              <td><a href="
              """)
        .concat(dashboardURL)
        .concat("""
            " target="_blank">""")
        .concat(dashboardURL)
        .concat("""
            </a>
                                        </td>
                                    </tr>
                                  </table>

                                </div>
                              </article>
                              <p>
                                Thank you for choosing Steelduxx.
                              </p>
                            </section>
                          </section>
                        </main>

                        <footer>
                          <section class="content has-text-centered">
                            <article class="buttons">
                              <a href="https://www.steelduxx.eu/contact/" target="_blank">
                                <button class="button">Contact us</button>
                              </a>
                            </article>
                            <p>&copy; 2024 copyright by Steelduxx</p>
                          </section>
                        </footer>
                      </center>
                    </body>

                    </html>
            """);

    MailUtil.sendEmail(sender, password, receiver, "Account registered", content);
  }
  // #endregion

  // #region Password Changed
  /**
   * Sends an email for password set with account details.
   *
   * @param receiver    the email address of the receiver
   * @param accountName the name of the account holder
   * @param passwordURL the URL for setting the password
   */
  public static void sendMailPasswordReset(String receiver, String accountName, String passwordURL) {

    String content = ""
        .concat("""
                    <!DOCTYPE html>
                    <html lang="en">

                    <head>
                      <meta charset="UTF-8">
                      <meta http-equiv="X-UA-Compatible" content="IE=edge">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>Steelduxx &bull; Password Reset</title>
                    </head>

                    <body>
                      <center>
                        <header>
                          <section class="hero is-primary">
                            <article class="hero-body">
                              <article class="container">
                                <h1 class="title">
                                  Password reset!
                                </h1>
                                <h2 class="subtitle">
                                  Please reset your Steelduxx password.
                                </h2>
                              </article>
                            </article>
                          </section>
                        </header>

                        <main>
                          <section class="outer">
                            <section class="inner box">
                              Dear
            """)
        .concat(accountName)
        .concat(
            """
                        ,
                        <br>
                        <br>
                        <p>
                          The password to your Steelduxx account needs to be reset.<br>
                          Please reset the password of your account by clicking the link below.
                        </p>
                    <article class="grid box">
                      <div class="details">
                        <p class="p">
                          Here are your account details:
                        </p>
                        <table>
                <tr>
                  <th style="text-align: left;">- <strong>Email: </strong></th>
                  <td>""")
        .concat(receiver)
        .concat("""
                </td>
            </tr>
            <tr>
              <th style="text-align: left;">- <strong>Password URL: </strong></th>
              <td><a href="
              """)
        .concat(passwordURL)
        .concat("""
            " target="_blank">""")
        .concat(passwordURL)
        .concat("""
            </a>
                                        </td>
                                    </tr>
                                  </table>

                                </div>
                              </article>
                              <p>
                                Please reset your password as soon as possible.
                              </p>
                            </section>
                          </section>
                        </main>

                        <footer>
                          <section class="content has-text-centered">
                            <article class="buttons">
                              <a href="https://www.steelduxx.eu/contact/" target="_blank">
                                <button class="button">Contact us</button>
                              </a>
                            </article>
                            <p>&copy; 2024 copyright by Steelduxx</p>
                          </section>
                        </footer>
                      </center>
                    </body>

                    </html>
            """);

    sendEmail(sender, password, receiver, "Password reset", content);
  }
  // #endregion

  // #region Order Status Update
  /**
   * Sends a mail with the order status update to the specified receiver.
   *
   * @param receiver    the email address of the receiver
   * @param accountName the name of the account holder
   * @param accountId   the ID of the account
   * @param orderId     the ID of the order
   * @param orderStatus the status of the order
   * @param orderURL    the URL of the order
   */
  public static void sendMailOrderStatusUpdate(String receiver, String accountName,
      String accountId, String orderId,
      String orderStatus, String orderURL) {
    String content = ""
        .concat("""
                    <!DOCTYPE html>
                    <html lang="en">

                    <head>
                      <meta charset="UTF-8">
                      <meta http-equiv="X-UA-Compatible" content="IE=edge">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>Steelduxx &bull; Order Status Update</title>
                    </head>

                    <body>
                      <center>
                        <header>
                          <section class="hero is-primary">
                            <article class="hero-body">
                              <article class="container">
                                <h1 class="title">
                                  Order status update!
                                </h1>
                                <h2 class="subtitle">
                                  The status of one of your orders has been updated.
                                </h2>
                              </article>
                            </article>
                          </section>
                        </header>

                        <main>
                          <section class="outer">
                            <section class="inner box">
                              Dear
            """)
        .concat(accountName)
        .concat(
            """
                    ,
                    <br>
                    <br>
                    <p>
                      One of your orders' status has been updated.<br>
                      You can view the updated status of your order by clicking the link below.
                    </p>
                <article class="grid box">
                  <div class="details">
                    <p class="p">
                      Here are your order details:
                    </p>
                    <table>
                      <tr>
                        <th style="text-align: left;">- <strong>Account ID: </strong></th>
                        <td>""")
        .concat(accountId)
        .concat("""
                </td>
            </tr>
            <tr>
              <th style="text-align: left;">- <strong>Email: </strong></th>
              <td>""")
        .concat(receiver)
        .concat("""
                </td>
            </tr>
            <tr>
              <th style="text-align: left;">- <strong>Order ID: </strong></th>
              <td>""")
        .concat(orderId)
        .concat(
            """
                    </td>
                </tr>
                <tr>
                  <th style="text-align: left;">- <strong>Order Status: </strong></th>
                  <td>""")
        .concat(orderStatus)
        .concat(
            """
                        </td>
                    </tr>
                <tr>
                  <th style="text-align: left;">- <strong>Order URL: </strong></th>
                  <td><a href="
                  """)
        .concat(orderURL)
        .concat("""
            " target="_blank">""")
        .concat(orderURL)
        .concat("""
            </a>
                                        </td>
                                    </tr>
                                  </table>

                                </div>
                              </article>
                              <p>
                                Please set your password as soon as possible.
                              </p>
                            </section>
                          </section>
                        </main>

                        <footer>
                          <section class="content has-text-centered">
                            <article class="buttons">
                              <a href="https://www.steelduxx.eu/contact/" target="_blank">
                                <button class="button">Contact us</button>
                              </a>
                            </article>
                            <p>&copy; 2024 copyright by Steelduxx</p>
                          </section>
                        </footer>
                      </center>
                    </body>

                    </html>
            """);

    sendEmail(sender, password, receiver, "Order Status Update", content);
  }
  // #endregion

  // #region Send Mail
  /**
   * A method to send an email to a specified receiver with the given sender name,
   * subject, and HTML body.
   *
   * @param sender   the email address of the sender
   * @param password the password of the sender
   * @param receiver the email address of the receiver
   * @param subject  the subject of the email
   * @param htmlBody the HTML body of the email
   */
  static void sendEmail(String sender, String password, String receiver, String subject,
      String htmlBody) {

    if (!mailRegexCheck(receiver)) {
      sentry.warn(new IllegalArgumentException("Invalid email address: " + receiver));
      return;
    }

    try {
      // Get the properties for the SMTP server, authenticate the sender and return
      // the created session object
      Session session = Session.getInstance(
          getProps(),
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(sender, password);
            }
          });

      // Create a new message and set the headers, sender, receiver, subject and body
      Transport.send(createMimeMessage(session, sender, "Steelduxx • No reply", receiver, subject, htmlBody));
      sentry.info("> Email sent successfully! - " + receiver + " - " + subject);
    } catch (MessagingException | UnsupportedEncodingException ex) {
      sentry.info("Sender: " + sender + ", password: " + password);
      sentry.warn(ex);
    }
  }
  // #endregion

  // #region Create Mime Message
  /**
   * A description of the entire Java function.
   *
   * @param session    description of parameter
   * @param sender     description of parameter
   * @param senderName description of parameter
   * @param receiver   description of parameter
   * @param subject    description of parameter
   * @param htmlBody   description of parameter
   * @return description of return value
   */
  private static Message createMimeMessage(Session session, String sender, String senderName, String receiver,
      String subject, String htmlBody) throws MessagingException, UnsupportedEncodingException {

    Message msg = new MimeMessage(session);
    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
    msg.addHeader("format", "flowed");
    msg.addHeader("Content-Transfer-Encoding", "8bit");
    msg.setReplyTo(InternetAddress.parse(receiver, false));
    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false));
    msg.setFrom(new InternetAddress(sender, senderName));
    msg.setSubject(subject);
    // msg.setText(body);
    msg.setContent(htmlBody, "text/html");

    return msg;
  }
  // #endregion

  // #region Get SMTP Server Properties
  /**
   * Get the properties for the SMTP server.
   * 
   * @return Properties
   */
  private static Properties getProps() {
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.office365.com");
    props.put("mail.smtp.port", 587);
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");
    props.put("mail.smtp.ssl.ciphers", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
    props.put("mail.smtp.auth", "true");
    props.put("mail.debug", "true");
    return props;
  }
  // #endregion

  // #region Mail Regex Check
  /**
   * A description of the entire Java function.
   *
   * @param receiver description of parameter
   * @return description of return value
   */
  private static boolean mailRegexCheck(String receiver) {
    String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    return Pattern.compile(regexPattern)
        .matcher(receiver)
        .matches();
  }
  // #endregion
}
