/*
 */
package org.rapidbeans.security;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.Application;

/**
 * Test for class UserAuthnData.
 * 
 * @author Martin Bluemel
 */
public class UserAuthnDataTest extends TestCase {

	/**
	 * read test authn data.
	 */
	public void testReadAuthnDataDoc() {
		Application.setAuthnRoleType("org.rapidbeans.security.Role");
		Document doc = new Document("aunthn",
				new File("../org.rapidbeans/testdata/authn/authn01.xml"));
		User martin = (User) doc.findBean(
				"org.rapidbeans.security.User", "bluemel");
		assertEquals("Martin", martin.getFirstname());
		Collection<RapidEnum> roles = (Collection<RapidEnum>) martin.getRoles();
		assertEquals(1, roles.size());
		Iterator<RapidEnum> iterator = roles.iterator();
		RapidEnum admin = iterator.next();
		assertEquals("Administrator", admin.name());
	}

	/**
	 * write some new authn data.
	 * 
	 * @throws MalformedURLException
	 */
	public void testWriteAuthnDataDoc() throws MalformedURLException {
		UserAuthnData authndata = new UserAuthnData();
		User martin = new User("bluemel");
		martin.setLastname("Martin");
		martin.setFirstname("Bluemel");
		authndata.addUser(martin);
		List<Role> rolesList = new ArrayList<Role>();
		rolesList.add(Role.Administrator);
		authndata.setRoles(rolesList);
		Document doc = new Document("authn", authndata);
		File testfile = new File("../org.rapidbeans/testdata/authn/test01.xml");
		doc.setUrl(testfile.toURI().toURL());
		doc.save();
		testfile.delete();
	}
}
