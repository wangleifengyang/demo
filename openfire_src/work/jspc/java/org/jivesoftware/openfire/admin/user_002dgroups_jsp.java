package org.jivesoftware.openfire.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.openfire.PresenceManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.security.SecurityAuditManager;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import gnu.inet.encoding.Stringprep;
import org.jivesoftware.util.LocaleUtils;
import org.jivesoftware.util.Log;
import org.jivesoftware.util.ParamUtils;
import org.jivesoftware.util.StringUtils;
import org.xmpp.packet.JID;
import org.xmpp.packet.Presence;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public final class user_002dgroups_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_fmt_message_key_nobody;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_fmt_message_key_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_fmt_message_key_nobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n<!-- Define Administration Bean -->\n");
      org.jivesoftware.util.WebManager webManager = null;
      synchronized (_jspx_page_context) {
        webManager = (org.jivesoftware.util.WebManager) _jspx_page_context.getAttribute("webManager", PageContext.PAGE_SCOPE);
        if (webManager == null){
          webManager = new org.jivesoftware.util.WebManager();
          _jspx_page_context.setAttribute("webManager", webManager, PageContext.PAGE_SCOPE);
        }
      }
      out.write('\n');

	webManager.init(pageContext);

      out.write('\n');
      out.write('\n');

	// Get parameters
 	String add = StringUtils.escapeHTMLTags(ParamUtils.getParameter(request, "add"));
	String delete = StringUtils.escapeHTMLTags(ParamUtils.getParameter(request, "delete"));
	boolean success = ParamUtils.getBooleanParameter(request,"updatesuccess");
	String username = StringUtils.escapeHTMLTags(ParamUtils.getParameter(request, "username"));
	JID jid = webManager.getXMPPServer().createJID(username, null);
	
    if(add != null) {
    	try {
    		Group group = webManager.getGroupManager().getGroup(add);
    		group.getMembers().add(jid);
    		response.sendRedirect("user-groups.jsp?username=" + URLEncoder.encode(username, "UTF-8") + "&updatesuccess=true");
	    } catch (GroupNotFoundException exp) {
	    	return;
	    }
    }
    
    if(delete != null) {
    	try {
    		Group group = webManager.getGroupManager().getGroup(delete);
    		group.getMembers().remove(jid);
    		response.sendRedirect("user-groups.jsp?username=" + URLEncoder.encode(username, "UTF-8") + "&updatesuccess=true");
	    } catch (GroupNotFoundException exp) {
	    	return;
	    }
    }
	
    // Load the user object
    User user = null;
    try {
        user = webManager.getUserManager().getUser(username);
    }
    catch (UserNotFoundException unfe) {
    }
    
    Collection<Group> userGroups = webManager.getGroupManager().getGroups(user);
    int start = ParamUtils.getIntParameter(request,"start",0);
    int range = ParamUtils.getIntParameter(request,"range",15);
    
    if (request.getParameter("range") != null) {
        webManager.setRowsPerPage("group-summary", range);
    }

    ArrayList<Group> groups = new ArrayList<Group>(webManager.getGroupManager().getGroups());
    // Remove already joined groups 
    groups.removeAll(userGroups);
    
    int groupCount = groups.size();
    int groupIndex = start + range;

    String search = null;
    if (webManager.getGroupManager().isSearchSupported() && request.getParameter("search") != null
            && !request.getParameter("search").trim().equals("")) {
        search = request.getParameter("search");
        search = StringUtils.escapeHTMLTags(search);
        // Use the search terms to get the list of groups.
        groups = new ArrayList<Group>(webManager.getGroupManager().search(search));
        // Count already joined groups in the search result 
        int userGroupCount = 0;
		for(Group group : groups) {
	if(userGroups.contains(group)) {
		userGroupCount++;
	}
		}
        groups.removeAll(userGroups);
        groupCount = groups.size() - userGroupCount;
    }
    
    if(groupIndex >= groupCount) {
    	groupIndex = groupCount;
    }

    // paginator vars
    int numPages = (int)Math.ceil((double)groupCount/(double)range);
    int curPage = (start/range) + 1;
	
    if(success) {

      out.write("\n<div class=\"jive-success\">\n\t<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n\t\t<tbody>\n\t\t\t<tr>\n\t\t\t\t<td class=\"jive-icon\"><img src=\"images/success-16x16.gif\"\n\t\t\t\t\twidth=\"16\" height=\"16\" border=\"0\" alt=\"\"></td>\n\t\t\t\t<td class=\"jive-icon-label\">");
      if (_jspx_meth_fmt_message_0(_jspx_page_context))
        return;
      out.write("</td>\n\t\t\t</tr>\n\t\t</tbody>\n\t</table>\n</div>\n<br>\n");

	}

      out.write("\n\n<html>\n<head>\n<title>");
      if (_jspx_meth_fmt_message_1(_jspx_page_context))
        return;
      out.write("</title>\n<meta name=\"subPageID\" content=\"user-groups\" />\n<meta name=\"extraParams\"\n\tcontent=\"");
      out.print("username="+URLEncoder.encode(username, "UTF-8"));
      out.write("\" />\n</head>\n<body>\n\t<p>\n\t\t");
      if (_jspx_meth_fmt_message_2(_jspx_page_context))
        return;
      out.write("\n\t\t<b>");
      out.print(username);
      out.write(".</b>\n\t</p>\n\t<div class=\"jive-table\">\n\t\t<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n\t\t\t<thead>\n\t\t\t\t<tr>\n\t\t\t\t\t<th>&nbsp;</th>\n\t\t\t\t\t<th>");
      if (_jspx_meth_fmt_message_3(_jspx_page_context))
        return;
      out.write("</th>\n\t\t\t\t\t<th>");
      if (_jspx_meth_fmt_message_4(_jspx_page_context))
        return;
      out.write("</th>\n\t\t\t\t</tr>\n\t\t\t</thead>\n\t\t\t<tbody>\n\t\t\t\t");

					// Print the list of groups
															if (userGroups.isEmpty()) {
				
      out.write("\n\t\t\t\t<tr>\n\t\t\t\t\t<td align=\"center\" colspan=\"6\">");
      if (_jspx_meth_fmt_message_5(_jspx_page_context))
        return;
      out.write("</td>\n\t\t\t\t</tr>\n\n\t\t\t\t");

					}
															int x = 0;
															for (Group group : userGroups) {
																String groupName = URLEncoder.encode(group.getName(), "UTF-8");
																x++;
				
      out.write("\n\t\t\t\t<tr class=\"jive-");
      out.print((((x % 2) == 0) ? "even" : "odd"));
      out.write("\">\n\t\t\t\t\t<td width=\"1%\" valign=\"top\">");
      out.print(x);
      out.write("</td>\n\t\t\t\t\t<td><a href=\"group-edit.jsp?group=");
      out.print(groupName);
      out.write('"');
      out.write('>');
      out.print(StringUtils.escapeHTMLTags(group.getName()));
      out.write("</a>\n\t\t\t\t\t\t");

							if (group.getDescription() != null) {
						
      out.write(" <br> <span class=\"jive-description\"> ");
      out.print(StringUtils.escapeHTMLTags(group.getDescription()));
      out.write("\n\t\t\t\t\t</span> ");

 	}
 
      out.write("</td>\n\n\t\t\t\t\t<td width=\"5%\"><a\n\t\t\t\t\t\thref=\"user-groups.jsp?username=");
      out.print(URLEncoder.encode(user.getUsername(), "UTF-8"));
      out.write("&delete=");
      out.print(groupName);
      out.write("\"\n\t\t\t\t\t\ttitle=\"");
      if (_jspx_meth_fmt_message_6(_jspx_page_context))
        return;
      out.write("\"><img\n\t\t\t\t\t\t\tsrc=\"images/delete-16x16.gif\" width=\"16\" height=\"16\" border=\"0\"\n\t\t\t\t\t\t\talt=\"");
      if (_jspx_meth_fmt_message_7(_jspx_page_context))
        return;
      out.write("\"></a></td>\n\t\t\t\t</tr>\n\t\t\t\t");

					}
				
      out.write("\n\t\t\t</tbody>\n\t\t</table>\n\t</div>\n\t<br />\n\n\t<p>\n\t\t");
      if (_jspx_meth_fmt_message_8(_jspx_page_context))
        return;
      out.write("\n\t\t<b>");
      out.print(username);
      out.write(".</b>\n\t</p>\n\t");

		if (webManager.getGroupManager().isSearchSupported()) {
	
      out.write("\n\n\t<form action=\"user-groups.jsp\" method=\"get\" name=\"searchForm\">\n\t\t<table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n\t\t\t<tr>\n\t\t\t\t<td valign=\"bottom\">");
      if (_jspx_meth_fmt_message_9(_jspx_page_context))
        return;
      out.write(" <b>");
      out.print(groupCount);
      out.write("</b></td>\n\t\t\t\t<td align=\"right\" valign=\"bottom\">");
      if (_jspx_meth_fmt_message_10(_jspx_page_context))
        return;
      out.write(": <input type=\"text\" size=\"30\"\n\t\t\t\t\tmaxlength=\"150\" name=\"search\"\n\t\t\t\t\tvalue=\"");
      out.print(((search!=null) ? search : ""));
      out.write("\"></td>\n\t\t\t</tr>\n\t\t</table>\n\t\t<input type=\"hidden\" name=\"username\"\n\t\t\tvalue=\"");
      out.print(StringUtils.escapeForXML(user.getUsername()));
      out.write("\">\n\t</form>\n\n\t<script language=\"JavaScript\" type=\"text/javascript\">\n\t\tdocument.searchForm.search.focus();\n\t</script>\n\n\t");

		}
		// Otherwise, searching is not supported.
		else {
	
      out.write("\n\t<p>\n\t\t");
      if (_jspx_meth_fmt_message_11(_jspx_page_context))
        return;
      out.write("\n\t\t<b>");
      out.print(groupCount);
      out.write("</b>\n\t\t");

			if (numPages > 1) {
		
      out.write("\n\n\t\t,\n\t\t");
      if (_jspx_meth_fmt_message_12(_jspx_page_context))
        return;
      out.write('\n');
      out.write('	');
      out.write('	');
      out.print((start + 1));
      out.write('-');
      out.print((start + range));
      out.write("\n\n\t\t");

			}
		
      out.write("\n\t</p>\n\t");

		}
	
      out.write('\n');
      out.write('\n');
      out.write('	');

		if (numPages > 1) {
	
      out.write("\n\n\t<p>\n\t\t");
      if (_jspx_meth_fmt_message_13(_jspx_page_context))
        return;
      out.write("\n\t\t[\n\t\t");

			for (int i = 0; i < numPages; i++) {
					String sep = ((i + 1) < numPages) ? " " : "";
					boolean isCurrent = (i + 1) == curPage;
		
      out.write("\n\t\t<a\n\t\t\thref=\"user-groups.jsp?username=");
      out.print(StringUtils.escapeForXML(user.getUsername()));
      out.write("&start=");
      out.print((i * range));
      out.print(search != null ? "&search=" + URLEncoder.encode(search, "UTF-8") : "");
      out.write("\"\n\t\t\tclass=\"");
      out.print(((isCurrent) ? "jive-current" : ""));
      out.write('"');
      out.write('>');
      out.print((i + 1));
      out.write("</a>");
      out.print(sep);
      out.write("\n\n\t\t");

			}
		
      out.write("\n\t\t]\n\t</p>\n\n\t");

		}
	
      out.write("\n\n\t<div class=\"jive-table\">\n\t\t<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n\t\t\t<thead>\n\t\t\t\t<tr>\n\t\t\t\t\t<th>&nbsp;</th>\n\t\t\t\t\t<th nowrap>");
      if (_jspx_meth_fmt_message_14(_jspx_page_context))
        return;
      out.write("</th>\n\t\t\t\t\t<th nowrap>");
      if (_jspx_meth_fmt_message_15(_jspx_page_context))
        return;
      out.write("</th>\n\t\t\t\t</tr>\n\t\t\t</thead>\n\t\t\t<tbody>\n\n\t\t\t\t");

					// Print the list of groups
					if (groups.isEmpty()) {
				
      out.write("\n\t\t\t\t<tr>\n\t\t\t\t\t<td align=\"center\" colspan=\"6\">");
      if (_jspx_meth_fmt_message_16(_jspx_page_context))
        return;
      out.write("</td>\n\t\t\t\t</tr>\n\n\t\t\t\t");

					}
					int i = 0;
					for (Group group : groups.subList(start, groupIndex)) {
						String groupName = URLEncoder.encode(group.getName(), "UTF-8");
						i++;
				
      out.write("\n\t\t\t\t<tr class=\"jive-");
      out.print((((i % 2) == 0) ? "even" : "odd"));
      out.write("\">\n\t\t\t\t\t<td width=\"1%\" valign=\"top\">");
      out.print(i);
      out.write("</td>\n\t\t\t\t\t<td><a href=\"group-edit.jsp?group=");
      out.print(groupName);
      out.write('"');
      out.write('>');
      out.print(StringUtils.escapeHTMLTags(group.getName()));
      out.write("</a>\n\t\t\t\t\t\t");

							if (group.getDescription() != null) {
						
      out.write(" <br> <span class=\"jive-description\"> ");
      out.print(StringUtils.escapeHTMLTags(group.getDescription()));
      out.write("\n\t\t\t\t\t</span> ");

 	}
 
      out.write("</td>\n\n\t\t\t\t\t<td width=\"5%\"><a\n\t\t\t\t\t\thref=\"user-groups.jsp?username=");
      out.print(URLEncoder.encode(user.getUsername(), "UTF-8"));
      out.write("&add=");
      out.print(groupName);
      out.write("\"\n\t\t\t\t\t\ttitle=\"");
      if (_jspx_meth_fmt_message_17(_jspx_page_context))
        return;
      out.write("\"> <img\n\t\t\t\t\t\t\tsrc=\"images/add-16x16.gif\" width=\"16\" height=\"16\" border=\"0\"\n\t\t\t\t\t\t\talt=\"");
      if (_jspx_meth_fmt_message_18(_jspx_page_context))
        return;
      out.write("\"></a></td>\n\t\t\t\t</tr>\n\t\t\t\t");

					}
				
      out.write("\n\t\t\t</tbody>\n\t\t</table>\n\t</div>\n\n\t");

		if (numPages > 1) {
	
      out.write("\n\t<br>\n\t<p>\n\t\t");
      if (_jspx_meth_fmt_message_19(_jspx_page_context))
        return;
      out.write("\n\t\t[\n\t\t");

			for (i = 0; i < numPages; i++) {
					String sep = ((i + 1) < numPages) ? " " : "";
					boolean isCurrent = (i + 1) == curPage;
		
      out.write("\n\t\t<a\n\t\t\thref=\"user-groups.jsp?username=");
      out.print(StringUtils.escapeForXML(user.getUsername()));
      out.write("&start=");
      out.print((i * range));
      out.print(search != null ? "&search=" + URLEncoder.encode(search, "UTF-8") : "");
      out.write("\"\n\t\t\tclass=\"");
      out.print(((isCurrent) ? "jive-current" : ""));
      out.write('"');
      out.write('>');
      out.print((i + 1));
      out.write("</a>");
      out.print(sep);
      out.write("\n\n\t\t");

			}
		
      out.write("\n\t\t]\n\t</p>\n\n\t");

		}
	
      out.write("\n\n</body>\n</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_fmt_message_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_0 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_0.setParent(null);
    _jspx_th_fmt_message_0.setKey("user.groups.form.update");
    int _jspx_eval_fmt_message_0 = _jspx_th_fmt_message_0.doStartTag();
    if (_jspx_th_fmt_message_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
    return false;
  }

  private boolean _jspx_meth_fmt_message_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_1 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_1.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_1.setParent(null);
    _jspx_th_fmt_message_1.setKey("user.groups.title");
    int _jspx_eval_fmt_message_1 = _jspx_th_fmt_message_1.doStartTag();
    if (_jspx_th_fmt_message_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_1);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_1);
    return false;
  }

  private boolean _jspx_meth_fmt_message_2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_2 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_2.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_2.setParent(null);
    _jspx_th_fmt_message_2.setKey("user.groups.member.info");
    int _jspx_eval_fmt_message_2 = _jspx_th_fmt_message_2.doStartTag();
    if (_jspx_th_fmt_message_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
    return false;
  }

  private boolean _jspx_meth_fmt_message_3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_3 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_3.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_3.setParent(null);
    _jspx_th_fmt_message_3.setKey("user.groups.name");
    int _jspx_eval_fmt_message_3 = _jspx_th_fmt_message_3.doStartTag();
    if (_jspx_th_fmt_message_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_3);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_3);
    return false;
  }

  private boolean _jspx_meth_fmt_message_4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_4 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_4.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_4.setParent(null);
    _jspx_th_fmt_message_4.setKey("global.delete");
    int _jspx_eval_fmt_message_4 = _jspx_th_fmt_message_4.doStartTag();
    if (_jspx_th_fmt_message_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_4);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_4);
    return false;
  }

  private boolean _jspx_meth_fmt_message_5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_5 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_5.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_5.setParent(null);
    _jspx_th_fmt_message_5.setKey("group.summary.no_groups");
    int _jspx_eval_fmt_message_5 = _jspx_th_fmt_message_5.doStartTag();
    if (_jspx_th_fmt_message_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_5);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_5);
    return false;
  }

  private boolean _jspx_meth_fmt_message_6(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_6 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_6.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_6.setParent(null);
    _jspx_th_fmt_message_6.setKey("global.click_delete");
    int _jspx_eval_fmt_message_6 = _jspx_th_fmt_message_6.doStartTag();
    if (_jspx_th_fmt_message_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_6);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_6);
    return false;
  }

  private boolean _jspx_meth_fmt_message_7(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_7 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_7.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_7.setParent(null);
    _jspx_th_fmt_message_7.setKey("global.click_delete");
    int _jspx_eval_fmt_message_7 = _jspx_th_fmt_message_7.doStartTag();
    if (_jspx_th_fmt_message_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_7);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_7);
    return false;
  }

  private boolean _jspx_meth_fmt_message_8(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_8 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_8.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_8.setParent(null);
    _jspx_th_fmt_message_8.setKey("user.groups.info");
    int _jspx_eval_fmt_message_8 = _jspx_th_fmt_message_8.doStartTag();
    if (_jspx_th_fmt_message_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_8);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_8);
    return false;
  }

  private boolean _jspx_meth_fmt_message_9(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_9 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_9.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_9.setParent(null);
    _jspx_th_fmt_message_9.setKey("group.summary.total_group");
    int _jspx_eval_fmt_message_9 = _jspx_th_fmt_message_9.doStartTag();
    if (_jspx_th_fmt_message_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_9);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_9);
    return false;
  }

  private boolean _jspx_meth_fmt_message_10(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_10 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_10.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_10.setParent(null);
    _jspx_th_fmt_message_10.setKey("group.summary.search");
    int _jspx_eval_fmt_message_10 = _jspx_th_fmt_message_10.doStartTag();
    if (_jspx_th_fmt_message_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_10);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_10);
    return false;
  }

  private boolean _jspx_meth_fmt_message_11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_11 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_11.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_11.setParent(null);
    _jspx_th_fmt_message_11.setKey("group.summary.total_group");
    int _jspx_eval_fmt_message_11 = _jspx_th_fmt_message_11.doStartTag();
    if (_jspx_th_fmt_message_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_11);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_11);
    return false;
  }

  private boolean _jspx_meth_fmt_message_12(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_12 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_12.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_12.setParent(null);
    _jspx_th_fmt_message_12.setKey("global.showing");
    int _jspx_eval_fmt_message_12 = _jspx_th_fmt_message_12.doStartTag();
    if (_jspx_th_fmt_message_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_12);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_12);
    return false;
  }

  private boolean _jspx_meth_fmt_message_13(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_13 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_13.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_13.setParent(null);
    _jspx_th_fmt_message_13.setKey("global.pages");
    int _jspx_eval_fmt_message_13 = _jspx_th_fmt_message_13.doStartTag();
    if (_jspx_th_fmt_message_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_13);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_13);
    return false;
  }

  private boolean _jspx_meth_fmt_message_14(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_14 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_14.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_14.setParent(null);
    _jspx_th_fmt_message_14.setKey("user.groups.name");
    int _jspx_eval_fmt_message_14 = _jspx_th_fmt_message_14.doStartTag();
    if (_jspx_th_fmt_message_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_14);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_14);
    return false;
  }

  private boolean _jspx_meth_fmt_message_15(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_15 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_15.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_15.setParent(null);
    _jspx_th_fmt_message_15.setKey("global.add");
    int _jspx_eval_fmt_message_15 = _jspx_th_fmt_message_15.doStartTag();
    if (_jspx_th_fmt_message_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_15);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_15);
    return false;
  }

  private boolean _jspx_meth_fmt_message_16(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_16 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_16.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_16.setParent(null);
    _jspx_th_fmt_message_16.setKey("group.summary.no_groups");
    int _jspx_eval_fmt_message_16 = _jspx_th_fmt_message_16.doStartTag();
    if (_jspx_th_fmt_message_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_16);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_16);
    return false;
  }

  private boolean _jspx_meth_fmt_message_17(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_17 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_17.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_17.setParent(null);
    _jspx_th_fmt_message_17.setKey("global.click_add");
    int _jspx_eval_fmt_message_17 = _jspx_th_fmt_message_17.doStartTag();
    if (_jspx_th_fmt_message_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_17);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_17);
    return false;
  }

  private boolean _jspx_meth_fmt_message_18(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_18 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_18.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_18.setParent(null);
    _jspx_th_fmt_message_18.setKey("global.click_add");
    int _jspx_eval_fmt_message_18 = _jspx_th_fmt_message_18.doStartTag();
    if (_jspx_th_fmt_message_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_18);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_18);
    return false;
  }

  private boolean _jspx_meth_fmt_message_19(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_19 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_19.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_19.setParent(null);
    _jspx_th_fmt_message_19.setKey("global.pages");
    int _jspx_eval_fmt_message_19 = _jspx_th_fmt_message_19.doStartTag();
    if (_jspx_th_fmt_message_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_19);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_19);
    return false;
  }
}
