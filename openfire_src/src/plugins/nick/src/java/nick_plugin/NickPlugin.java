package nick_plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.dom4j.Element;
import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.disco.IQDiscoInfoHandler;
import org.jivesoftware.util.StringUtils;
import org.jivesoftware.util.WebManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Message.Type;


public class NickPlugin implements Component,Plugin{
	private static final Logger Log = LoggerFactory.getLogger(NickPlugin.class);
	private ComponentManager componentManager;
	private PluginManager pluginManager;
	private final String serviceName="nick";
	private final String from="system";
	
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		pluginManager=manager;
		componentManager=ComponentManagerFactory.getComponentManager();
		try {
			componentManager.addComponent(serviceName, this);
		} catch (ComponentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroyPlugin() {
		pluginManager=null;
	}

	@Override
	public String getDescription() {
		return pluginManager.getDescription(this);
	}

	@Override
	public String getName() {
		return pluginManager.getName(this);
	}

	@Override
	public void initialize(JID arg0, ComponentManager arg1)
			throws ComponentException {
		
	}
	
	
	/**
	 * manager message
	 */
	@Override
	public void processPacket(Packet packet) {
		if(packet instanceof IQ){
			//register component
			IQ 	replyPacket=handleIQ((IQ)packet);
			try {
				componentManager.sendPacket(this, replyPacket);
			} catch (ComponentException e) {
				e.printStackTrace();
			}
		}else if(packet instanceof Message){
			handleMessage((Message)packet);
		}
	}

	/**
	 * manage message type information
	 * 
	 */
	private void handleMessage(Message message){
		String domain=new WebManager().getXMPPServer().getInstance().getServerInfo().getXMPPDomain();
		String body=message.getBody();
		Type type=message.getType();
		JID from=message.getFrom();
		String[] ss=body.split(":");
		if("0".equals(ss[2])){ //modify self name
			updateSelfName(ss[0].trim(),ss[1].trim());
		}else if("1".equals(ss[2])){
			updateFriendName(from.getNode(),ss[0].trim(),ss[1].trim());
		}
	}
	/**
	 * manage component register information
	 * @param iq
	 * @return
	 */
	private IQ handleIQ(IQ iq){
		final Element childElement=iq.getChildElement();
		final String namespace=childElement.getNamespaceURI();
		if(namespace.equals(IQDiscoInfoHandler.NAMESPACE_DISCO_INFO)){
			IQ replyPacket=IQ.createResultIQ(iq);
			Element responseElement=replyPacket.setChildElement("query", IQDiscoInfoHandler.NAMESPACE_DISCO_INFO);
			responseElement.addElement("identity").addAttribute("category", "push").addAttribute("type", "user").addAttribute("name", "Server Push");
			return replyPacket;
		}
		return null;
	}
	@Override
	public void shutdown() {
		
	}

	@Override
	public void start() {
		
	}
	
	/**
	 * modify self nick
	 * @param username
	 * @param name
	 */
	public void updateSelfName(String username,String name) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
        	Date now=new Date();
            con = DbConnectionManager.getConnection();
            con.setAutoCommit(false);
            con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);
            String sql="update ofuser set name=?,modificationDate=? where username=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, StringUtils.dateToMillis(now));
            pstmt.setString(3, username);
            pstmt.executeUpdate();
            con.commit();
        }
        catch (Exception e) {
        	try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
            Log.warn("Error trying to update a new row in ofuser: ", e);
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
    }
	
	public void updateFriendName(String userName,String friendName,String name){
		 Connection con = null;
	        PreparedStatement pstmt = null;
	        try {
	        	Date now=new Date();
	            con = DbConnectionManager.getConnection();
	            con.setAutoCommit(false);
	            con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);
	            String sql="update ofroster set nick=? where username=? and jid like ?";
	            pstmt = con.prepareStatement(sql);
	            pstmt.setString(1, name);
	            pstmt.setString(2, userName);
	            pstmt.setString(3, friendName+"@%");
	            pstmt.executeUpdate();
	            con.commit();
	        }
	        catch (Exception e) {
	        	try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	            Log.warn("Error trying to update a new row in ofroster: ", e);
	        }
	        finally {
	            DbConnectionManager.closeConnection(pstmt, con);
	        }
	}
}
