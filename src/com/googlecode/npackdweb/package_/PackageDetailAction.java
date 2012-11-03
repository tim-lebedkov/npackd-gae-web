package com.googlecode.npackdweb.package_;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.npackdweb.NWUtils;
import com.googlecode.npackdweb.Package;
import com.googlecode.npackdweb.wlib.Action;
import com.googlecode.npackdweb.wlib.ActionSecurityType;
import com.googlecode.npackdweb.wlib.Page;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * A package.
 */
public class PackageDetailAction extends Action {
	/**
	 * -
	 */
	public PackageDetailAction() {
		super("^/p/([^/]+)$", ActionSecurityType.ANONYMOUS);
	}

	@Override
	public Page perform(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String name = req.getRequestURI().substring(3);

		Objectify ofy = ObjectifyService.begin();
		Package r = ofy.find(new Key<Package>(Package.class, name));
		if (r == null) {
			r = new Package(name);
		}

		UserService us = UserServiceFactory.getUserService();
		return new PackageDetailPage(r, NWUtils.isEditorLoggedIn());
	}
}