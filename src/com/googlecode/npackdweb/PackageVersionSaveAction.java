package com.googlecode.npackdweb;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * Save or create a package.
 */
public class PackageVersionSaveAction extends Action {
	/**
	 * -
	 */
	public PackageVersionSaveAction() {
		super("^/package-version/save$", ActionSecurityType.ADMINISTRATOR);
	}

	@Override
	public Page perform(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String name = req.getParameter("name");
		NWUtils.LOG.severe(name);
		Objectify ofy = ObjectifyService.begin();
		PackageVersion p;
		if (name == null || name.trim().length() == 0) {
			p = new PackageVersion();
			p.name = name;
			int pos = name.indexOf("@");
			p.package_ = name.substring(0, pos);
			p.version = name.substring(pos + 1);
		} else {
			p = ofy.find(new Key<PackageVersion>(PackageVersion.class, name));
			if (p == null) {
				p = new PackageVersion();
				p.name = name;
				int pos = name.indexOf("@");
				p.package_ = name.substring(0, pos);
				p.version = name.substring(pos + 1);
			}
		}
		p.url = req.getParameter("url");
		p.sha1 = req.getParameter("sha1");
		p.detectMSI = req.getParameter("detectMSI");
		p.oneFile = "one-file".equals(req.getParameter("type"));
		p.tags = NWUtils.split(req.getParameter("tags"));
		List<String> lines = NWUtils.splitLines(req
				.getParameter("importantFiles"));
		p.importantFilePaths.clear();
		p.importantFileTitles.clear();
		for (String line : lines) {
			int pos = line.indexOf(" ");
			if (pos > 0) {
				String path = line.substring(0, pos);
				String title = line.substring(pos + 1);
				p.importantFilePaths.add(path);
				p.importantFileTitles.add(title);
			}
		}
		ofy.put(p);
		resp.sendRedirect("/p/" + p.package_);
		return null;
	}
}
