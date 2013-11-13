package com.github.cqljmeter.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;

public class CassandraClusterConfig extends ConfigTestElement  implements TestStateListener, TestBean {

	private static final Logger log = LoggingManager.getLoggerForClass();
	private static final long serialVersionUID = -3927956370607660166L;
	
	private String clusterId = "";
	private String contactPoint = "";
	private String user = "";
	private String password = "";

	@Override
	public void testEnded() {
		((ClusterHolder)getThreadContext().getVariables().getObject(getClusterId())).shutdown();
	}

	@Override
	public void testEnded(String arg0) {
		testEnded();
	}

	@Override
	public void testStarted() {
		log.debug("Creating cluster: " + clusterId);
		Builder builder = Cluster.builder().withClusterName(clusterId).addContactPoint(contactPoint);
		if (StringUtils.isNotBlank(user)) {
			builder = builder.withCredentials(user, password);
		}
		getThreadContext().getVariables().putObject(getClusterId(), new ClusterHolder(builder.build()));
	}

	@Override
	public void testStarted(String arg0) {
		testStarted();
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getContactPoint() {
		return contactPoint;
	}

	public void setContactPoint(String contactPoint) {
		this.contactPoint = contactPoint;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
