import java.util.*;
import javax.naming.*;
import javax.naming.ldap.*;
import javax.naming.directory.*;
import java.io.Console;
import javax.naming.directory.SearchControls;
import java.text.MessageFormat;

public class LdapAuthentication {

  public static boolean authenticateJndi(String username, String password)
      throws Exception {
    Properties props = new Properties();
    props.put(Context.INITIAL_CONTEXT_FACTORY,
              "com.sun.jndi.ldap.LdapCtxFactory");
    props.put(Context.PROVIDER_URL, "ldaps://ldap-vip.sns.gov:636/");

    InitialDirContext context = new InitialDirContext(props);

    SearchControls ctrls = new SearchControls();
    ctrls.setReturningAttributes(new String[] {"givenName", "sn", "memberOf"});
    ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

    NamingEnumeration<javax.naming.directory.SearchResult> answers =
        context.search("dc=sns,dc=ornl,dc=gov", "(uid=" + username + ")",
                       ctrls);
    javax.naming.directory.SearchResult result = answers.nextElement();

    String user = result.getNameInNamespace();
    // just a simple search for all the attributes
    System.out.println(result.getAttributes());

    try {
      props = new Properties();
      props.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
      props.put(Context.PROVIDER_URL, "ldaps://ldap-vip.sns.gov:636/");
      props.put(Context.SECURITY_PRINCIPAL, user);
      props.put(Context.SECURITY_CREDENTIALS, password);
      context = new InitialDirContext(props);

    } catch (Exception e) {
      System.out.println("*** Failed authentication");
      return false;
    }
    System.out.println("*** User authenticated");
    return true;
  }
  public static void main(String[] args) throws Exception {
    final String username = "rhf";
    final String password = new String(
        System.console().readPassword("User '%s' password> ", username));
    authenticateJndi(username, password);
  }
}
