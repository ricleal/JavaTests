import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;
import javax.naming.directory.Attributes;
/*
Run as:

java -Djavax.net.ssl.keyStore="$J2REDIR/lib/security/cacerts" \
-Djavax.net.ssl.keyStorePassword=changeit  JndiAuthDemo
*/

public class JndiAuthDemo {
    public static void main(String args[]) {

        final String username = "rhf";
        final String bindPassword = new String(System.console().readPassword("User '%s' password> ", username));

        String serverURL = "ldaps://ldap-vip.sns.gov:636/";
        String bindDN = String.format("uid=%s,ou=users,dc=sns,dc=ornl,dc=gov", username);

        Properties parms = new Properties();
        parms.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        parms.put(Context.PROVIDER_URL, serverURL);
        parms.put(Context.SECURITY_PROTOCOL, "ssl");
        parms.put(Context.SECURITY_AUTHENTICATION, "simple");
        parms.put(Context.SECURITY_PRINCIPAL, bindDN);
        parms.put(Context.SECURITY_CREDENTIALS, bindPassword);

        DirContext ctx = null;

        try {
            ctx = new InitialDirContext(parms);
            System.err.println("Successful authenticated bind");

            SearchControls ctls = new SearchControls();
            String[] attrIDs = { "cn", "memberOf" };
            ctls.setReturningAttributes(attrIDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            // Get the groups of this user
            NamingEnumeration answer = ctx.search("ou=Groups,dc=sns,dc=ornl,dc=gov",
                    String.format("(&(memberUid=%s)(objectClass=posixGroup))", username), ctls);
            while (answer.hasMore()) {
                SearchResult rslt = (SearchResult) answer.next();
                Attributes attrs = rslt.getAttributes();
                String group = attrs.get("cn").toString();
                System.out.println(group);
            }

        } catch (NamingException ne) {
            System.err.println("Unsuccessful authenticated bind\n");
            ne.printStackTrace(System.err);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ne) {
                }
            }
        }
    }
}