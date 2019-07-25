import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/*
	
$ sudo keytool -keystore $J2REDIR/lib/security/cacerts -import -alias certificate -file ~/git/WebReduction/config/certificates/ldap.crt

default password: changeit

Run as:
$ 1
java -Djavax.net.ssl.keyStore="$J2REDIR/lib/security/cacerts" \
-Djavax.net.ssl.keyStorePassword=changeit LdapSearch


*/

public class LdapSearch {
  public static void main(String[] args) throws Exception {
    
    Hashtable env = new Hashtable();

    String sp = "com.sun.jndi.ldap.LdapCtxFactory";
    env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

    String ldapUrl = "ldaps://ldap-vip.sns.gov:636/dc=sns,dc=ornl,dc=gov";
    env.put(Context.PROVIDER_URL, ldapUrl);
    
    DirContext dctx = new InitialDirContext(env);

    String base = "ou=Groups";

    SearchControls sc = new SearchControls();
    String[] attributeFilter = { "cn", "description" };
    sc.setReturningAttributes(attributeFilter);
    sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

    String filter = "(&(cn=IPTS*)(description=proposal)(memberUid=*))";

    NamingEnumeration results = dctx.search(base, filter, sc);
    while (results.hasMore()) {
      SearchResult sr = (SearchResult) results.next();
      Attributes attrs = sr.getAttributes();

      Attribute attr = attrs.get("cn");
      System.out.print(attr.get() + ": ");
      attr = attrs.get("description");
      System.out.println(attr.get());
    }
    dctx.close();
  }
}